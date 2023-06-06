package com.cch.do_question.util;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

//定义一些全局变量
public class Global {
    public static int question_size = 100;//题目数量
    public static String DB_PATH;// 数据库的路径
    public static String DB_NAME = "jkmj.sqlite";// 数据库的名称
    public static SQLiteDatabase db;//该类可以执行 SQL 语句，以完成对数据表的增加、修改、删除、查询等操作
    public static SharedPreferences initPreferences;//本地保存的信息
    public static String questi0nURl="http://150.158.83.151/question/";
}
