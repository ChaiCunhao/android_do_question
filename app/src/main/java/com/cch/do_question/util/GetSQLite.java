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

    //-------------下面是测试时的数据和方法----------------
    // 定义题目数组
    private String[] question_contents = {
            "问题1", "问题2", "问题3", "问题4", "问题5", "问题6", "问题7", "问题8", "问题9", "问题10",
            "问题11", "问题12", "问题13", "问题14", "问题15", "问题16", "问题17", "问题18", "问题19", "问题20",
            "问题21", "问题22", "问题23", "问题24", "问题25", "问题26", "问题27", "问题28", "问题29", "问题30",
            "问题31", "问题32", "问题33", "问题34", "问题35", "问题36", "问题37", "问题38", "问题39", "问题40",
            "问题41", "问题42", "问题43", "问题44", "问题45", "问题46", "问题47", "问题48", "问题49", "问题50",
            "问题51", "问题52", "问题53", "问题54", "问题55", "问题56", "问题57", "问题58", "问题59", "问题60",
            "问题61", "问题62", "问题63", "问题64", "问题65", "问题66", "问题67", "问题68", "问题69", "问题70",
            "问题71", "问题72", "问题73", "问题74", "问题75", "问题76", "问题77", "问题78", "问题79", "问题80",
            "问题81", "问题82", "问题83", "问题84", "问题85", "问题86", "问题87", "问题88", "问题89", "问题90",
            "问题91", "问题92", "问题93", "问题94", "问题95", "问题96", "问题97", "问题98", "问题99", "问题100",
    };
    // 定义答案数组
    private String[][] answers = {
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
            {"a", "b", "c"}, {"x", "y"}, {"111", "2222", "3333333", "44"}, {"a", "b", "c"}, {"d", "e", "f"}, {"ad", "wef", "agerg"}, {"a", "b", "123"}, {"a", "3452", "c"}, {"90214", "b", "c"}, {"11a", "11b", "11c"},
    };
    //答案
    private int[] rights = {
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
            2, 1, 4,1,2,3,1,2,3,2,
    };

    /* 把问题信息封装成集合，为后面调用数据库数据替换做准备*/
    public List<Question> setQuestionList() {
        List<Question> list = new ArrayList<>();
        for (int i = 0; i < question_contents.length; i++) {
            Question question=new Question();
            question.setID(i+1);
            question.setQuestion_content(question_contents[i]);
            List<String> answer_list = new ArrayList<>();
            for (int j = 0; j < answers[i].length; j++) {
                answer_list.add(answers[i][j]);
            }
            question.setAnswers(answer_list);
            question.setRight(rights[i]);
            list.add(question);
        }
        return list;
    }

    /* 把问题信息封装成集合，为后面调用数据库数据替换做准备*/
    public List<QuestionItem> setQuestionItemList() {
        List<QuestionItem> list2 = new ArrayList<>();
        for (int i = 0; i < question_contents.length; i++) {
            QuestionItem questionItem=new QuestionItem(i+1,-1,false,false);
            list2.add(questionItem);
        }
        return list2;
    }

}
