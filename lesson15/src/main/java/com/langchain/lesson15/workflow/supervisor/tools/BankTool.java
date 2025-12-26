package com.langchain.lesson15.workflow.supervisor.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.util.HashMap;
import java.util.Map;

public class BankTool {

    private final Map<String, Double> accounts = new HashMap<>();

    public void createAccount(String user, Double initialBalance) {
        if (accounts.containsKey(user)) {
            throw new RuntimeException("该 " + user + " 用户的账号已经存在");
        }
        accounts.put(user, initialBalance);
    }

    double getBalance(String user) {
        Double balance = accounts.get(user);
        if (balance == null) {
            throw new RuntimeException("未找到该用户的余额信息： " + user);
        }
        System.out.println("获取用户"+user+"的金额: "+balance);
        return balance;
    }

    @Tool("通过账号获取该账号存储的币种，并返回币种名称")
    String currency(@P("user name") String user) {
        return "美元";
    }

    @Tool("将给定的金额记入该用户账户，并返回新的余额")
    Double credit(@P("user name") String user, @P("amount") Double amount) {
        Double balance = accounts.get(user);
        if (balance == null) {
            throw new RuntimeException("未找到该用户的余额信息： " + user);
        }
        Double newBalance = balance + amount;
        accounts.put(user, newBalance);
        System.out.println("存入用户"+user+"的金额: "+amount +", 账号最终总额："+newBalance);
        return newBalance;
    }

    @Tool("按照指定的用户信息提取指定金额，并返回新的余额。")
    Double withdraw(@P("user name") String user, @P("amount") Double amount) {
        Double balance = accounts.get(user);
        if (balance == null) {
            throw new RuntimeException("未找到该用户的余额信息： " + user);
        }
        Double newBalance = balance - amount;
        accounts.put(user, newBalance);
        System.out.println("取出用户"+user+"的金额: "+amount +", 账号最终总额："+newBalance);
        return newBalance;
    }

}
