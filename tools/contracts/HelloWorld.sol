pragma solidity 0.8.7;

contract HelloWorld{
    uint256 private data;
    event SetData(uint256 args);
    function setData(uint256 n) external {
        data = n;
        require(tx.origin != address(0),"");
        emit SetData(n);
    }

    function getData() external view returns(uint256) {
        return data;
    }
}

