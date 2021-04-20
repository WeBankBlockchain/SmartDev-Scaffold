/**
 * Copyright 2014-2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.webank.scaffold;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.scaffold.artifact.ProjectArtifact;
import com.webank.scaffold.artifact.webase.NewMainDir.SolInfo;
import com.webank.scaffold.artifact.webase.NewMainResourceDir.ContractInfo;
import com.webank.scaffold.factory.ProjectFactory;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import org.junit.Test;

public class ProjectFactoryTest {

    // hello world
    String contractName = "HelloWorld";
    String solSourceCodeBase64Str = "cHJhZ21hIHNvbGlkaXR5IF4wLjQuMjsNCmNvbnRyYWN0IEhlbGxvV29ybGR7DQogICAgc3RyaW5nIG5hbWU7DQogICAgZXZlbnQgU2V0TmFtZShzdHJpbmcgbmFtZSk7DQogICAgZnVuY3Rpb24gZ2V0KCljb25zdGFudCByZXR1cm5zKHN0cmluZyl7DQogICAgICAgIHJldHVybiBuYW1lOw0KICAgIH0NCiAgICBmdW5jdGlvbiBzZXQoc3RyaW5nIG4pew0KICAgICAgICBlbWl0IFNldE5hbWUobik7DQogICAgICAgIG5hbWU9bjsNCiAgICB9DQp9";
    String abiStr = "[{\"constant\":false,\"inputs\":[{\"name\":\"n\",\"type\":\"string\"}],\"name\":\"set\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"name\",\"type\":\"string\"}],\"name\":\"SetName\",\"type\":\"event\"}]";
    String binStr = "608060405234801561001057600080fd5b50610373806100206000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680634ed3885e146100515780636d4ce63c146100ba575b600080fd5b34801561005d57600080fd5b506100b8600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061014a565b005b3480156100c657600080fd5b506100cf610200565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561010f5780820151818401526020810190506100f4565b50505050905090810190601f16801561013c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b7f4df9dcd34ae35f40f2c756fd8ac83210ed0b76d065543ee73d868aec7c7fcf02816040518080602001828103825283818151815260200191508051906020019080838360005b838110156101ac578082015181840152602081019050610191565b50505050905090810190601f1680156101d95780820380516001836020036101000a031916815260200191505b509250505060405180910390a180600090805190602001906101fc9291906102a2565b5050565b606060008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102985780601f1061026d57610100808354040283529160200191610298565b820191906000526020600020905b81548152906001019060200180831161027b57829003601f168201915b5050505050905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106102e357805160ff1916838001178555610311565b82800160010185558215610311579182015b828111156103105782518255916020019190600101906102f5565b5b50905061031e9190610322565b5090565b61034491905b80821115610340576000816000905550600101610328565b5090565b905600a165627a7a72305820baa9e2a7ab055843a8a3de62b50fba49e6309323fb92358b598491fa5a76b9e90029";
    String smBinStr = "";
    // build params
    String group = "org.webase.example";
    String artifactName = "demo_" + contractName.toLowerCase();
    String outputDir = "output";
    String sdkMapStr = "{\"sdk.key\":\"-----BEGIN PRIVATE KEY-----\\nMIGEAgEAMBAGByqGSM49AgEGBSuBBAAKBG0wawIBAQQgxqr/d/VgQ0fAr/KvyAeW\\nJ6bD1tqxZ5gYOdfIJiK7WOmhRANCAAT3g/OsuSAD2I/dKLWnZTbMGQ8l9WnkD/wr\\npyoiQkMy1qI5/3Sj4WFKGcVu9vhsd0nLoP+y1QttYKM0m5QGcuhP\\n-----END PRIVATE KEY-----\\n\",\"ca.crt\":\"-----BEGIN CERTIFICATE-----\\nMIIBsDCCAVagAwIBAgIJAPwQ7ISyofOIMAoGCCqGSM49BAMCMDUxDjAMBgNVBAMM\\nBWNoYWluMRMwEQYDVQQKDApmaXNjby1iY29zMQ4wDAYDVQQLDAVjaGFpbjAgFw0y\\nMTA0MDYxMjMwNDBaGA8yMTIxMDMxMzEyMzA0MFowNTEOMAwGA1UEAwwFY2hhaW4x\\nEzARBgNVBAoMCmZpc2NvLWJjb3MxDjAMBgNVBAsMBWNoYWluMFYwEAYHKoZIzj0C\\nAQYFK4EEAAoDQgAE6UcrK7ukGBVvBmWYwgIloM38ibqtxF2zBnM9zgU4bujjJU1Y\\nCZsHGKVGuNstSOZYfYulnTtFUoHhUEyhddvql6NQME4wHQYDVR0OBBYEFBBSyZi8\\nk/Hz/Q2SAin5bMnE1nOFMB8GA1UdIwQYMBaAFBBSyZi8k/Hz/Q2SAin5bMnE1nOF\\nMAwGA1UdEwQFMAMBAf8wCgYIKoZIzj0EAwIDSAAwRQIgEpuPZypVImOtDty9p50X\\njeD4wdgzHXpd3CDPui4CnZYCIQC4n+r97cCB51dPb+WjDNV5C18S2uI8LlNVj+xL\\ndSweAg==\\n-----END CERTIFICATE-----\\n\",\"sdk.crt\":\"-----BEGIN CERTIFICATE-----\\nMIIBeDCCAR+gAwIBAgIJAJoEtSMUsa8HMAoGCCqGSM49BAMCMDgxEDAOBgNVBAMM\\nB2FnZW5jeUExEzARBgNVBAoMCmZpc2NvLWJjb3MxDzANBgNVBAsMBmFnZW5jeTAg\\nFw0yMTA0MDYxMjMwNDBaGA8yMTIxMDMxMzEyMzA0MFowMTEMMAoGA1UEAwwDc2Rr\\nMRMwEQYDVQQKDApmaXNjby1iY29zMQwwCgYDVQQLDANzZGswVjAQBgcqhkjOPQIB\\nBgUrgQQACgNCAAT3g/OsuSAD2I/dKLWnZTbMGQ8l9WnkD/wrpyoiQkMy1qI5/3Sj\\n4WFKGcVu9vhsd0nLoP+y1QttYKM0m5QGcuhPoxowGDAJBgNVHRMEAjAAMAsGA1Ud\\nDwQEAwIF4DAKBggqhkjOPQQDAgNHADBEAiANbeRFiiS6mH+vcAOwV3wXd9YW/B2a\\n+vrHMm6NwtliRAIgRH4gSF0XLmpVOEO21bJFDGWm9siIX0cnj0R3kNGZcB4=\\n-----END CERTIFICATE-----\\n-----BEGIN CERTIFICATE-----\\nMIIBcTCCARegAwIBAgIJANrOZ+FrVNpIMAoGCCqGSM49BAMCMDUxDjAMBgNVBAMM\\nBWNoYWluMRMwEQYDVQQKDApmaXNjby1iY29zMQ4wDAYDVQQLDAVjaGFpbjAeFw0y\\nMTA0MDYxMjMwNDBaFw0zMTA0MDQxMjMwNDBaMDgxEDAOBgNVBAMMB2FnZW5jeUEx\\nEzARBgNVBAoMCmZpc2NvLWJjb3MxDzANBgNVBAsMBmFnZW5jeTBWMBAGByqGSM49\\nAgEGBSuBBAAKA0IABIqMDvvzvTq8WW1UtJrnnsifw9/OrPsMc9CrrYBsWdwOGhdx\\nfNTJA1ss+vngjrhAmWHczvbh+E1WOlDGzpCumeqjEDAOMAwGA1UdEwQFMAMBAf8w\\nCgYIKoZIzj0EAwIDSAAwRQIhALsAbAQ9BDeofk4VYzYx2ZAHB1HviDp9ndvXAkLN\\nsfHZAiAjViK97dDr3gxP/qHg0e8BG9ptEv7Do8caOPj33F+yOQ==\\n-----END CERTIFICATE-----\\n-----BEGIN CERTIFICATE-----\\nMIIBsDCCAVagAwIBAgIJAPwQ7ISyofOIMAoGCCqGSM49BAMCMDUxDjAMBgNVBAMM\\nBWNoYWluMRMwEQYDVQQKDApmaXNjby1iY29zMQ4wDAYDVQQLDAVjaGFpbjAgFw0y\\nMTA0MDYxMjMwNDBaGA8yMTIxMDMxMzEyMzA0MFowNTEOMAwGA1UEAwwFY2hhaW4x\\nEzARBgNVBAoMCmZpc2NvLWJjb3MxDjAMBgNVBAsMBWNoYWluMFYwEAYHKoZIzj0C\\nAQYFK4EEAAoDQgAE6UcrK7ukGBVvBmWYwgIloM38ibqtxF2zBnM9zgU4bujjJU1Y\\nCZsHGKVGuNstSOZYfYulnTtFUoHhUEyhddvql6NQME4wHQYDVR0OBBYEFBBSyZi8\\nk/Hz/Q2SAin5bMnE1nOFMB8GA1UdIwQYMBaAFBBSyZi8k/Hz/Q2SAin5bMnE1nOF\\nMAwGA1UdEwQFMAMBAf8wCgYIKoZIzj0EAwIDSAAwRQIgEpuPZypVImOtDty9p50X\\njeD4wdgzHXpd3CDPui4CnZYCIQC4n+r97cCB51dPb+WjDNV5C18S2uI8LlNVj+xL\\ndSweAg==\\n-----END CERTIFICATE-----\\n\"}";

    @Test
    public void testBuild() throws JsonProcessingException {
        String solSourceCode = new String(Base64.getDecoder().decode(solSourceCodeBase64Str));
        SolInfo solInfo = new SolInfo();
        solInfo.setContractName(contractName);
        solInfo.setSolRawString(solSourceCode);
        System.out.println("solInfo: ");
        System.out.println(solInfo);

        ContractInfo contractInfo = new ContractInfo();
        contractInfo.setContractName(contractName);
        contractInfo.setAbiStr(abiStr);
        contractInfo.setBinStr(binStr);
        contractInfo.setContractAddress("0xd5d4fcf2a46831510f095bfb447bc945f99309f7");
        System.out.println("contractInfo: ");
        System.out.println(contractInfo);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> sdkMap = mapper.readValue(sdkMapStr, Map.class);
        System.out.println("sdkMap: ");
        System.out.println(sdkMap);

        ProjectFactory projectFactory = new ProjectFactory();
        ProjectArtifact result = projectFactory.buildProjectDir(Collections.singletonList(solInfo),
            Collections.singletonList(contractInfo), group, artifactName, outputDir,
            //null, null, null, null);
            "127.0.0.1:25200", 2, "0x123", sdkMap);
        System.out.println("result: ");
        System.out.println(result);

    }
}
