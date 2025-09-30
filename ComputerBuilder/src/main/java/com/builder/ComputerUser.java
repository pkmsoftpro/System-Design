package com.builder;

/**
 * Created by prashant.mod on 20-09-2025 Saturday 1:06:12 am
 *
 */
public class ComputerUser {
  
  public static void main(String[] args) {
    Computer computer = new Computer.ComputerBuilder()
        .setCpu("cpu")
        .setRam("ram")
        .setStorage("storage")
        .build();
    System.out.println(computer);
    
  }

}
