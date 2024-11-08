package cn.popularSciencesManager.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/***
 * 窗体提示类，负责创建提示窗体
 */

public class JDialogPromptWindow {
    /***
     * Dialog窗体枚举类，控制Dialog提示框的选择
     * CLOSED_WINDOWS       关闭原窗口
     * NOT_CLOSED_WINDOWS   不关闭原窗口
     */
    public enum DialogSelect{
        /**
         * 关闭父窗口
         */
        CLOSED_WINDOWS,
        /***
         * 不关闭父窗口
         */
        NOT_CLOSED_WINDOWS,

        /***
         * 点击确认后关闭父窗口
         */

        CLICK_TO_CLOSED_WINDOWS,
        /***
         * 正常通知
         */
        NORMAL



    }


    /*
    * 提示窗口集合
    * */

    /***
     * 基于JFrame的提示窗口
     * @param jf    传入JFrame对象
     * @param info  传入要提示的信息
     */
    public void createInfoDialog(JFrame jf, String info, DialogSelect dg){
        JDialog jd=new JDialog(jf);
        switch (dg){
            case CLOSED_WINDOWS :
                dialogInfoClosedOwner(jd,info);break;
            case NOT_CLOSED_WINDOWS:
                dialogInfoNotClosedOwner(jd,info);break;
            case CLICK_TO_CLOSED_WINDOWS:
                dialogInfoChickClosedOwner(jd,info);break;
        }
    }

    /***
     * 基于JDialog的提示窗口
     * @param jdf   传入的JDialog对象
     * @param info  传入要提示的信息
     */

    public boolean createInfoDialog(JDialog jdf, String info, DialogSelect dg){
        JDialog jd=new JDialog(jdf);
        switch (dg){
            case CLOSED_WINDOWS :
                dialogInfoClosedOwner(jd,info);
                break;
            case NOT_CLOSED_WINDOWS:
                dialogInfoNotClosedOwner(jd,info);
                break;
            case CLICK_TO_CLOSED_WINDOWS:
                dialogInfoChickClosedOwner(jd,info);
                break;
        }
        return false;
    }

    /***
     * 提示窗口主体，不关闭父窗口
     * 调用此项时需要
     * @param jd    传入的JDialog对象
     * @param info  dialogInfo(jd,info);
     */

    private void dialogInfoNotClosedOwner(JDialog jd,String info){
        jd.setTitle("注意！");
        jd.setLayout(new BorderLayout());
        jd.setModal(true);//开启模态
        jd.setSize(300,100);//初始化位置
        jd.setLocation(150,50);
        JLabel jl=new JLabel(info,SwingConstants.CENTER);
        JPanel jp=new JPanel();
        JButton jb=new JButton("确定");
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//退出窗口
                jd.dispose();
            }
        });
        jp.add(jb);
        jd.add(jl,BorderLayout.CENTER);
        jd.add(jp,BorderLayout.SOUTH);
        jd.pack();
        jd.setLocationRelativeTo(null);
        jd.setVisible(true);
    }

    private void dialogInfoClosedOwner(JDialog jd,String info){
        jd.setTitle("注意！");
        jd.setLayout(new BorderLayout());
        jd.setModal(true);//开启模态
        jd.setSize(300,100);//初始化位置
        jd.setLocation(150,50);
        JLabel jl=new JLabel(info,SwingConstants.CENTER);
        JPanel jp=new JPanel();
        JButton jb=new JButton("确定");
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//退出窗口
                jd.getOwner().dispose();
            }

        });
        jd.addWindowListener(new WindowAdapter() {//设置窗体关闭时顺带关闭父窗口
            @Override
            public void windowClosing(WindowEvent e) {
                jd.getOwner().dispose();
            }
        });
        jp.add(jb);
        jd.add(jl,BorderLayout.CENTER);
        jd.add(jp,BorderLayout.SOUTH);
        jd.pack();
        jd.setLocationRelativeTo(null);
        jd.setVisible(true);
    }

    private void dialogInfoChickClosedOwner(JDialog jd,String info){
        jd.setTitle("注意！");
        jd.setLayout(new BorderLayout());
        jd.setModal(true);//开启模态
        jd.setSize(300,100);//初始化位置
        jd.setLocation(150,50);
        JLabel jl=new JLabel(info,SwingConstants.CENTER);
        JPanel jp=new JPanel();
        JButton jb=new JButton("确定");
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//退出窗口
                jd.getOwner().dispose();
            }

        });
        jp.add(jb);
        jd.add(jl,BorderLayout.CENTER);
        jd.add(jp,BorderLayout.SOUTH);
        jd.pack();
        jd.setLocationRelativeTo(null);
        jd.setVisible(true);
    }

}
