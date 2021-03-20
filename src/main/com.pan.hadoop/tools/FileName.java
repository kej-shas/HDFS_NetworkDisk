package com.pan.hadoop.tools;

import java.io.File;

public class FileName {
    public static void main(String[] args) {
        File file=new File("/root/bigdata/user/cloudpan/com.pan.hadoop/download/123/456/计划表.xlsx");
        System.out.println(file.getParentFile());
    }
}
