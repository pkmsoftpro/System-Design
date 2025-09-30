package com.builder;

/**
 * Created by prashant.mod on 20-09-2025 Saturday 12:57:52 am
 *
 */
public class Computer {

  private String cpu;
  private String ram;
  private String storage;
  

  public Computer(ComputerBuilder builder) {
    this.cpu = builder.cpu;
    this.ram = builder.ram;
    this.storage = builder.storage;
  }
  
  public String getCpu() {
    return cpu;
  }

  public String getRam() {
    return ram;
  }

  public String getStorage() {
    return storage;
  }
  

  @Override
  public String toString() {
    return "Computer [cpu=" + cpu + ", ram=" + ram + ", storage=" + storage + "]";
  }

  public static class ComputerBuilder {
    private String cpu;
    private String ram;
    private String storage;
    
    public ComputerBuilder setCpu(String cpu) {
      this.cpu = cpu;
      return this;
    }
    
    public ComputerBuilder setRam(String ram) {
      this.ram = ram;
      return this;
    }
    
    public ComputerBuilder setStorage(String storage) {
      this.storage = storage;
      return this;
    }
    
    public Computer build() {
      return new Computer(this);
    }
  
  }

}
