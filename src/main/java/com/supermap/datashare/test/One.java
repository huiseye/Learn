package com.supermap.datashare.test;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class One {

    @Test
    public void test() {
        String dir = "C:\\Users\\ksf\\Desktop\\本地测试\\foler";
        File file = new File(dir);
        File[] files = file.listFiles();
        for (File f : files) {
            System.out.println(f.getName());
        }
        String newdir = "C:\\Users\\ksf\\Desktop\\本地测试\\foler.zip";
        file.renameTo(new File(newdir));

    }



    public static void compress(ZipOutputStream out, File sourceFile, String base) throws Exception {

        if (sourceFile.isDirectory()) {
            base = base.length() == 0 ? "" : base + File.separator;
            File[] files = sourceFile.listFiles();
            if (ArrayUtils.isEmpty(files)) {
                // todo 打包空目录
                // out.putNextEntry(new ZipEntry(base));
                return;
            }
            for (File file : files) {
                compress(out, file, base + file.getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            try (FileInputStream in = new FileInputStream(sourceFile)) {
                IOUtils.copy(in, out);
            } catch (Exception e) {
                throw new RuntimeException("打包异常: " + e.getMessage());
            }
        }
    }

    @Test
    public void test2() throws IOException {
        String dir = "C:\\Users\\ksf\\Desktop\\本地测试\\folder";
        String path = "C:\\Users\\ksf\\Desktop\\本地测试\\folder\\two.zip";
//        File file = new File(path);
//        if(!file.exists())
//            file.createNewFile();

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(path));
        out.putNextEntry(new ZipEntry(dir));
        String sourceFile = "C:\\Users\\ksf\\Desktop\\本地测试\\folder\\a.txt";
        FileInputStream in = new FileInputStream(sourceFile);
        IOUtils.copy(in, out);

    }

    @Test
    public void test3() throws IOException {
        //File.separator代表了当前系统的文件分割符
        //linux使用/分割符会报错找不到文件,window系统下/和\都可以作为文件路径
        System.out.println("File.separator="+ File.separator);

        File file = new File("D:" + File.separator + "upgrade46.txt");
        File zipFile = new File("d:" + File.separator + "hello.zip");
        //读取相关的文件
        InputStream input = new FileInputStream(file);
        //设置输出流
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(
                zipFile));

        zipOut.putNextEntry(new ZipEntry(file.getName()));
        // 设置注释
        zipOut.setComment("hello");
        int temp = 0;
        //读取相关的文件
        while((temp = input.read()) != -1){
            //写入输出流中
            zipOut.write(temp);
        }
        //关闭流
        input.close();
        zipOut.close();
    }


    /***
     * 将多个文件压缩成zip文件,ZipEntry的作用就是设置目录，zip文件里的目录。所以想
     * 保留目录结构就必须要有zipentry 成功
     * @throws IOException
     */
    @Test
    public void test4() throws IOException {
        //File.separator代表了当前系统的文件分割符
        //linux使用/分割符会报错找不到文件,window系统下/和\都可以作为文件路径
        System.out.println("File.separator="+ File.separator);

        File file = new File("C:\\Users\\ksf\\Desktop\\本地测试\\folder\\a.txt");
       File bfile = new File("C:\\Users\\ksf\\Desktop\\本地测试\\folder\\b.txt");
        File cfile = new File("C:\\Users\\ksf\\Desktop\\本地测试\\folder\\c.txt");
        File zipFile = new File("C:\\Users\\ksf\\Desktop\\本地测试\\folder\\show.zip");
        //读取相关的文件
        InputStream input = new FileInputStream(file);
        //设置输出流
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(
                zipFile));

        zipOut.putNextEntry(new ZipEntry(file.getName()));
        // 设置注释
        zipOut.setComment("hello");
        int temp = 0;
        //读取相关的文件
        while((temp = input.read()) != -1){
            //写入输出流中
            zipOut.write(temp);
        }
        zipOut.putNextEntry(new ZipEntry((bfile.getName())));
        input = new FileInputStream(bfile);
        while((temp = input.read()) != -1){
            //写入输出流中
            zipOut.write(temp);
        }

        zipOut.putNextEntry(new ZipEntry((cfile.getName())));
        input = new FileInputStream(cfile);
        while((temp = input.read()) != -1){
            //写入输出流中
            zipOut.write(temp);
        }
        //关闭流
        input.close();
        zipOut.close();
    }

    @Test
    public void test5() throws IOException {
        File file = new File("C:\\Users\\ksf\\Desktop\\本地测试\\folder/a.txt");
        File zipFile = new File("C:\\Users\\ksf\\Desktop\\本地测试\\yuri.zip");
        InputStream input = new FileInputStream(file);
        //设置输出流
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(
                zipFile));
        zipOut.putNextEntry(new ZipEntry("one/"+file.getName()));
        // 设置注释
        zipOut.setComment("花蜜");
        int temp = 0;
        //读取相关的文件
        while((temp = input.read()) != -1){
            //写入输出流中
            zipOut.write(temp);
        }
        input.close();
        zipOut.close();


    }





}
