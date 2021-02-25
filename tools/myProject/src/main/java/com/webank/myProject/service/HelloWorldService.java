package com.webank.myProject.service;

import com.webank.myProject.model.bo.HelloWorldSetInputBO;
import java.lang.Exception;
import java.lang.String;
import java.util.Arrays;
import lombok.Getter;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;

public class HelloWorldService {
  public static final String ABI = com.webank.myProject.contracts.HelloWorld.ABI;

  public static final String BINARY = com.webank.myProject.contracts.HelloWorld.BINARY;

  public static final String SM_BINARY = com.webank.myProject.contracts.HelloWorld.BINARY;

  @Getter
  private String address;

  private Client client;

  AssembleTransactionProcessor txProcessor;

  public HelloWorldService(String address, Client client) throws Exception {
    this.client = client;
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
    this.address = address;
  }

  public HelloWorldService(Client client) throws Exception {
    this.client = client;
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
    this.address = this.txProcessor.deployAndGetResponse(ABI,this.client.getCryptoType()==0?BINARY:SM_BINARY).getContractAddress();
  }

  public TransactionResponse set(HelloWorldSetInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "set", input.toArgs());
  }

  public CallResponse get() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "get", Arrays.asList());
  }
}
