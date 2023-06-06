package com.cch.do_question.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.cch.do_question.R;
import com.cch.do_question.bean.Question;
import com.cch.do_question.bean.QuestionItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GetSQLite {

    //初始化数据库以及打开指定的数据库,并获取数据库表及其相关信息
    public GetSQLite(Context context) {
        Global.initPreferences = context.getSharedPreferences("dbInfo", Context.MODE_PRIVATE);// 初始化数据库保存信息
        boolean isInitDB = Global.initPreferences.getBoolean("initDB", false);
        if (!isInitDB) {//如果没有初始化数据库
            initDB(context);//如果是第一次使用，则执行初始化
            Global.initPreferences.edit().putString("dbPath", Global.DB_PATH).commit();
            Global.initPreferences.edit().putBoolean("initDB", true).commit();//保存数据并提交
        } else {
            Global.DB_PATH = Global.initPreferences.getString("dbPath", "");
        }
        System.out.println("本地数据库路径：" + Global.DB_PATH);
    }

    public void initDB(Context context) {//初始化数据库
        File externalDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);//存储路径
        String dbPath = externalDir.getAbsolutePath() + File.separator + "db";//获取该路径下的 db 文件夹路径
        File dir = new File(dbPath);//定义db文件夹
        if (!dir.exists()) {// 如果文件夹不存在，则创建文件夹
            dir.mkdir();//创建文件夹
        }
        File file = new File(dir.getPath(), Global.DB_NAME);//定义数据库文件
        if (!file.exists()) {//如果文件不存在，则创建文件
            try {
                file.createNewFile();//创建文件
            } catch (IOException e) {
                System.out.println("创建文件时出错！");
                e.printStackTrace();
            }
            copyFile(context, R.raw.jkmj, file);//复制资源，将 raw 文件夹下的资源复制到存储卡中
        }
        Global.DB_PATH = file.getAbsolutePath();//获取数据库的路径
    }

    public void copyFile(Context context, int resId, File file) {//将 R.raw 中的数据库文件复制到存储卡中
        try {
            InputStream is = context.getResources().openRawResource(R.raw.jkmj);//打开文件对应的输入流
            FileOutputStream fos = new FileOutputStream(file);//打开文件对应的输出流
            byte[] buffer = new byte[1024];
            int hasRead = 0;
            while ((hasRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, hasRead);
            }
            fos.close();
            is.close();// 关闭资源
        } catch (Exception e) {
            System.out.println("复制文件时出错啦！");
        }
    }

    public SQLiteDatabase getDataBase(Context context) {//打开指定的数据库
        return Global.db = context.openOrCreateDatabase(Global.DB_PATH, Context.MODE_PRIVATE, null);
    } //打开数据库

    public void updateQuestionItem(Context context,QuestionItem questionItem){//更新一条答题情况
        SQLiteDatabase db = getDataBase(context);
        String selected_Index = String.valueOf(questionItem.getSelected_Index());
        String answered = String.valueOf(questionItem.isAnswered() ? 1 : 0);
        String correct = String.valueOf(questionItem.isCorrect() ? 1:0);
        String questionID = String.valueOf(questionItem.getQuestionNumber());
        db.execSQL("update questionitem set selected_Index=?,answered=?,correct=? where questionID=?",new String[]{selected_Index,answered,correct,questionID});
        db.close();
    }

    public void resetQuestionItem(Context context){//重置全部答题情况
        SQLiteDatabase db = getDataBase(context);
        db.execSQL("update questionitem set selected_Index=-1,answered=0,correct=0");
        db.close();
    }

    public List<QuestionItem> getQuestionItems(Context context) {//获取所有答题情况
        List<QuestionItem> questionItems = new ArrayList<>();
        SQLiteDatabase db = getDataBase(context);
        Cursor cursor = db.rawQuery("select * from questionItem", null);
        while (cursor.moveToNext()) {
            int questionID=cursor.getInt(cursor.getColumnIndex("questionID"));
            int selected_Index=cursor.getInt(cursor.getColumnIndex("selected_Index"));
            boolean answered=(1 == cursor.getInt(cursor.getColumnIndex("answered")));
            boolean correct=(1 == cursor.getInt(cursor.getColumnIndex("correct")));
            QuestionItem questionItem =new QuestionItem(questionID,selected_Index,answered,correct);
            questionItems.add(questionItem);
        }
        cursor.close();
        db.close();
        return questionItems;
    }

    public int getCorrectNumber(Context context) {//获取答题正确个数
        SQLiteDatabase db = getDataBase(context);
        Cursor cursor = db.rawQuery("select * from questionItem where answered=? and correct=?", new String[]{"1","1"});
        int correct_number = cursor.getCount();
        cursor.close();
        db.close();
        return correct_number;
    }

    public int getWrongNumber(Context context) {//获取答题错误个数
        SQLiteDatabase db = getDataBase(context);
        Cursor cursor = db.rawQuery("select * from questionItem where answered=? and correct=?", new String[]{"1","0"});
        int wrong_number = cursor.getCount();
        cursor.close();
        db.close();
        return wrong_number;
    }

    public int getNoAnswerNumber(Context context) {//获取未答题个数
        SQLiteDatabase db = getDataBase(context);
        Cursor cursor = db.rawQuery("select * from questionItem where answered=?", new String[]{"0"});
        int no_answer_number = cursor.getCount();
        cursor.close();
        db.close();
        return no_answer_number;
    }

}
