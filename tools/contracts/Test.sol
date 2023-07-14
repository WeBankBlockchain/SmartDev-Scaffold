pragma solidity >=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

contract Test {
    string name;
    struct Info {
        string i;
        A[] aList;
    }
    struct A {
        uint256[] a;
    }


    function setA(A memory info) public returns(string) {
        return name;
    }
    function setInfo(Info memory info) public returns(string) {
        return name;
    }

    constructor() public {
                //  require(false, "test");
    name = "Hello, World!";
    }

    function get() public view returns (string memory) {
             require(false, "test");
   return name;
    }

    function setB(bool[]  n) public returns(bool[] ) {
        return n;
    }
        function setBool(bool n, bool[] nArr) public returns(bool,bool[]){
        return (n,nArr);
    }

    function set(string memory n) public {
            require(false, "test");
    name = n;
    }

    function set32(uint32 n)public returns (uint32)  {
        return n;//12
    }
    function setBytes(bytes memory n)public returns (bytes memory)  {
        return n;//12
    }
}