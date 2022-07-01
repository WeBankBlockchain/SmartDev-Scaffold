pragma solidity>=0.6.10 <0.8.20;
pragma experimental ABIEncoderV2;

import "./KVTable.sol";

//数据供应商信息
contract ProviderInfo {
    //event
    event RegisterEvent (
        int256 ret,
        string indexed ProviderID,
        string indexed ProviderName,
        string indexed ProviderSCC
    );
    event ChangeEvent (
        int256 ret,
        string indexed ProviderID,
        string  Old_ProviderName,
        string  Old_ProviderSCC,
        string indexed New_ProviderName,
        string indexed New_ProviderSCC
    );
    event UnknownEvent (
        int256 RetCode,
        string indexed ActionName,
        string indexed ErrorMsg
    );
   struct ProviderInfomation{
        string provider_id;
        string provider_name;
        string provider_scc;
   }
    KVTable tf;
    //构造函数
    constructor () public {
        // 创建数据供应商信息表
        address kvAddr = address(0x1009);
        tf = KVTable(kvAddr);
        // 供应商ID 供应商上链名称 社会信用代码
        // 供应商信息表 key id ,field name,socialCreditCode
        //创建表
        tf.createTable("t_providerinfo", "provider_id", "provider_name,provider_scc");
    }
    /*根据id 查询供应商信息
    参数：
        provider_id:供应商id
    返回值:
        参数一：成功返回0，账户不存在返回-1
        参数二,三：第一个参数为0时有效，返回供应商名称，供应商社会信用编码
    */
    function select(string memory provider_id) public view returns (bool,string memory,string memory) {
        //查询
        Entry memory entry;
        bool result;
        (result,entry) = tf.get("t_providerinfo",provider_id);
        //长度不为2条
        if(entry.fields.length != 2) {
            return (false,"","");
        }
        //供应商名称及社会信用编码不能为空
        if(bytes(entry.fields[0].value).length == 0 || bytes(entry.fields[1].value).length == 0){
            return (false,"","");
        }
        return (true,entry.fields[0].value,entry.fields[1].value);
    }
    /* 供应商注册
    参数:
        provider_id : 供应商id
        provider_name : 供应商名称
        provider_scc : 供应商社会信息编码
    返回值:
        0 资产注册成功
        -1 资产账户已存在
        -2 其他错误
     */
     function register(string memory provider_id,string memory provider_name,string memory provider_scc)
     public
     returns (int256)
     {
        int256 ret_code = 0;
        bool ret = true;
        ProviderInfomation memory provider;
        //查询账号是否存在
        (ret,provider.provider_name,provider.provider_scc) = select(provider_id);
        if(ret != true){
            KVField memory kv1 = KVField("provider_name",provider_name);
            KVField memory kv2 = KVField("provider_scc",provider_scc);
            KVField[] memory KVFields = new KVField[](2);
            KVFields[0] = kv1;
            KVFields[1] = kv2;
            Entry memory entry = Entry(KVFields);

            //插入数据
            int256 count = tf.set("t_providerinfo",provider_id,entry);
            if(count == 1){
                //成功
                ret_code = 0;
            } else {
                // 失败 无权限或者其他错误
                emit UnknownEvent(count,"providerinfo_register","register other error");
                ret_code = -2;
            }
        } else {
            ret_code = -1;
        }
        emit RegisterEvent(ret_code,provider_id,provider_name,provider_scc);
        return ret_code;
     }
     /*
     描述: 修改供应商信息
     参数: 
        provider_id : 供应商id
        provider_name : 供应商名称
        provider_scc : 供应商社会信息编码
    返回值: 
         0 信息修改成功
        -1 供应商不存在
        -2 其他错误
      */
    function changeInfo(string memory provider_id,string memory provider_name,string memory provider_scc)
    public returns(int256) {
        bool ret = true;
        int256 ret_code = 0;
        ProviderInfomation memory provider;
        //查询账号是否存在
        (ret,provider.provider_name,provider.provider_scc) = select(provider_id);
        if(ret){
            KVField memory kv1 = KVField("provider_name",provider_name);
            KVField memory kv2 = KVField("provider_scc",provider_scc);
            KVField[] memory KVFields = new KVField[](2);
            KVFields[0] = kv1;
            KVFields[1] = kv2;
            Entry memory entry = Entry(KVFields);

            //插入数据
            int256 count = tf.set("t_providerinfo",provider_id,entry);
            if(count == 1){
                //成功
                ret_code = 0;
                emit ChangeEvent(ret_code,provider_id,provider.provider_name,provider.provider_scc
                ,provider_name,provider_scc);
            } else {
                // 失败 无权限或者其他错误
                emit UnknownEvent(count,"providerinfo_change","register other error");
                ret_code = -2;
            }
        } else {
            ret_code = -1;
        }
        
        return ret_code;
    }
}