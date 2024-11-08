package cn.popularSciencesManager.JFrame;

import cn.popularSciencesManager.model.UserModel;
import cn.popularSciencesManager.utils.BatchGeneration;
import cn.popularSciencesManager.utils.DbUtils;
import cn.popularSciencesManager.utils.JDialogPromptWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

public class Main {
    String URL = "127.0.0.1:3306";
    String databaseName = "sciences";
    String url = "jdbc:mysql://" + URL + "/" + databaseName + "?serverTimezone=GMT%2B8  &  useSSL=true & characterEncoding=utf8";
    String username = "root";
    String password = "12345600O";
    Connection con = DriverManager.getConnection(url, username, password);
    JDialogPromptWindow jdpw=new JDialogPromptWindow();
    UserModel user=new UserModel();
    DbUtils dbUtils=new DbUtils(con,databaseName);
    static Map<String, String[]> databaseStruct;//数据表，用于存储表格与
    BatchGeneration batchGeneration = new BatchGeneration();
    static Main main;
    static {
        try {
            main = new Main();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    //当前状态




    public Main() throws SQLException {
    }

    public Main(Connection con, String databaseName) throws SQLException {
        this.con=con;
        this.databaseName=databaseName;
    }

    public void login() {
        JFrame loginDialog = new JFrame("登录");
        loginDialog.setSize(200,150);
        loginDialog.setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new FlowLayout());
        loginDialog.add(loginPanel, BorderLayout.CENTER);

        JPanel loginMainPanel = new JPanel(new BorderLayout());
        loginPanel.add(loginMainPanel);



        JPanel loginLabelPanel1 = new JPanel(new GridLayout(2, 1));
        JPanel loginField = new JPanel(new GridLayout(2, 1));
        JPanel loginLabelPanel2 = new JPanel(new GridLayout(2, 1));
        JPanel loginButton = new JPanel(new FlowLayout());

        loginMainPanel.add(loginLabelPanel1, BorderLayout.WEST);
        loginMainPanel.add(loginField, BorderLayout.CENTER);
        loginMainPanel.add(loginLabelPanel2, BorderLayout.EAST);
        loginMainPanel.add(loginButton, BorderLayout.SOUTH);


        JLabel[] loginLabel = new JLabel[4];

        loginLabel[0] = new JLabel("账   号");
        loginLabel[1] = new JLabel("密   码");
        loginLabel[2] = new JLabel("注册账号");
        loginLabel[3] = new JLabel("忘记密码");

        JButton loginCheck = new JButton("登录");
        JButton loginCancel = new JButton("取消");

        JTextField accountNumber = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        //账号密码登录区
        loginLabelPanel1.add(loginLabel[0]);
        loginLabelPanel1.add(loginLabel[1]);

        //功能区域
        loginLabelPanel2.add(loginLabel[2]);
        loginLabelPanel2.add(loginLabel[3]);

        //数据填写区
        loginField.add(accountNumber);
        loginField.add(passwordField);

        //按钮区域
        loginButton.add(loginCheck);
        loginButton.add(loginCancel);

        loginLabel[2].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                main.register();
                loginDialog.dispose();
            }
        });

        loginLabel[3].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                main.forgotPass();
                loginDialog.dispose();
            }
        });

        //确认登录按钮
        loginCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql="select * from user where userID = ? and password = ?";
                try {
                    PreparedStatement pre=con.prepareStatement(sql);
                    if(accountNumber.getText().isEmpty()){
                        jdpw.createInfoDialog(loginDialog, "账号为空", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                        pre.setNull(1, Types.VARCHAR);
                        return;
                    }
                    else{
                        pre.setString(1,accountNumber.getText());
                    }
                    if(passwordField.getPassword().length==0){
                        jdpw.createInfoDialog(loginDialog, "密码为空", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                        pre.setNull(2, Types.VARCHAR);
                        return;
                    }
                    else {
                        pre.setString(2,new String(passwordField.getPassword()));
                    }
                    ResultSet rs=pre.executeQuery();
                    if(rs.next()){
                        if(rs.getString("userType").equals("管理员")){
                            user.setUserID(rs.getInt("userID"));
                            user.setUserName(rs.getString("userName"));
                            user.setCreateData(rs.getString("createData"));
                            user.setEmail(rs.getString("email"));
                            user.setUserType(rs.getString("userType"));
                            new ManagerFrame(user,con);
                            loginDialog.dispose();
                        }
                        else {
                            user.setUserID(rs.getInt("userID"));
                            user.setUserName(rs.getString("userName"));
                            user.setCreateData(rs.getString("createData"));
                            user.setEmail(rs.getString("email"));
                            user.setUserType(rs.getString("userType"));
                            new UserFrame(user,con);
                            loginDialog.dispose();
                        }
                    }
                    else
                        jdpw.createInfoDialog(loginDialog, "用户名或密码错误", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //取消按钮
        loginCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginDialog.dispose();
            }
        });
        loginDialog.setLocationRelativeTo(null);
        loginDialog.setVisible(true);
    }

    private void register(){
        JFrame registerUser=new JFrame();
        registerUser.setTitle("添加新用户");
        registerUser.setLayout(new FlowLayout());
        JPanel addTypePanel=new JPanel(new BorderLayout());
        registerUser.add(addTypePanel);


        JPanel[] topPanel=batchGeneration.createJPanel(3);
        topPanel[0].setLayout(new GridLayout(1,1));
        topPanel[1].setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel topLabel=new JLabel("请输入新用户的信息");
        topPanel[1].add(topLabel);
        topPanel[0].add(topPanel[1]);
        topPanel[2].setLayout(new FlowLayout(FlowLayout.RIGHT));
        topPanel[0].add(topPanel[2]);
        addTypePanel.add(topPanel[0],BorderLayout.NORTH);//添加顶部到主窗口


        //设置中心主显示页面
        //设置垂直盒子
        JPanel centerPanel=new JPanel(new FlowLayout());
        Box centerBox=Box.createVerticalBox();
        centerPanel.add(centerBox);
        addTypePanel.add(centerPanel,BorderLayout.CENTER);

        JPanel[] infoTypeIn=batchGeneration.createJPanel(4);
        for(JPanel panel:infoTypeIn){
            panel.setLayout(new GridLayout(1,2));
        }

        JLabel[] infoLabel=batchGeneration.createJLabel(new String[]{"用户名","密码","请再次输入密码","E-Mail(可选，修改密码用)"});
        JTextField[] infoField=batchGeneration.createJTextField(2,10);
        JPasswordField passwordField=new JPasswordField(10);
        JPasswordField passwordField1=new JPasswordField(10);

        infoTypeIn[0].add(infoLabel[0]);
        infoTypeIn[0].add(infoField[0]);
        centerBox.add(infoTypeIn[0]);
        infoTypeIn[1].add(infoLabel[1]);
        infoTypeIn[1].add(passwordField);
        centerBox.add(infoTypeIn[1]);
        infoTypeIn[2].add(infoLabel[2]);
        infoTypeIn[2].add(passwordField1);
        centerBox.add(infoTypeIn[2]);
        infoTypeIn[3].add(infoLabel[3]);
        infoTypeIn[3].add(infoField[1]);
        centerBox.add(infoTypeIn[3]);
        //底部窗口创建
        JPanel bottomArea=new JPanel(new FlowLayout());
        JButton[] southButton=batchGeneration.createJButton(new String[]{"创建账号","取消"});
        bottomArea.add(southButton[0]);
        bottomArea.add(southButton[1]);
        addTypePanel.add(bottomArea,BorderLayout.SOUTH);

        //创建账号按钮
        southButton[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql;
                // 检查密码是否一致
                if(!Arrays.equals(passwordField.getPassword(), passwordField1.getPassword())){
                    jdpw.createInfoDialog(registerUser,"两次输入的密码不一致", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                    return;
                }
                // 检查账号是否输入完整
                if(infoField[0].getText().isEmpty()){
                    jdpw.createInfoDialog(registerUser,"请输入账号", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                    return;
                }
                try{
                    // 插入用户信息
                    sql = "INSERT INTO user VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement pre = con.prepareStatement(sql);
                    pre.setNull(1, Types.INTEGER); // 用户ID系统自动生成
                    pre.setString(2, infoField[0].getText()); // 用户名
                    pre.setDate(3, Date.valueOf(LocalDate.now())); // 日期
                    pre.setString(4, new String(passwordField.getPassword())); // 用户密码
                    if (infoField[1].getText().isEmpty()) {
                        pre.setNull(5, Types.VARCHAR); // 用户email
                    } else {
                        pre.setString(5, infoField[1].getText());
                    }
                    pre.setString(6, "用户");

                    // 执行插入操作
                    if(pre.executeUpdate() == 0){
                        jdpw.createInfoDialog(registerUser,"用户名重复", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                        return;
                    }

                    // 查询刚插入的用户ID并显示注册成功信息
                    sql = "SELECT userID FROM user WHERE userName = ?";
                    pre = con.prepareStatement(sql);
                    pre.setString(1, infoField[0].getText());
                    ResultSet rs = pre.executeQuery();
                    if(rs.next()) {
                        jdpw.createInfoDialog(registerUser, String.format("注册成功!!!\n登录ID：%s", rs.getString("userID")), JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                        main.login();
                        registerUser.dispose();
                    } else {
                        jdpw.createInfoDialog(registerUser, "注册成功，但未能获取用户ID", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                    }

                    pre.close();
                } catch (SQLException ex){
                    ex.printStackTrace();
                    jdpw.createInfoDialog(registerUser,"用户名重复", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
            }
        });


        //取消按钮
        southButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.login();
                registerUser.dispose();
            }
        });
        registerUser.pack();
        registerUser.setLocationRelativeTo(null);
        registerUser.setVisible(true);
    }


    private void forgotPass(){
        final int[] userID = new int[1];
        final boolean[] flag = {false};
        JFrame forgotPass=new JFrame();
        forgotPass.setTitle("查找密码");
        forgotPass.setLayout(new FlowLayout());
        JPanel addTypePanel=new JPanel(new BorderLayout());
        forgotPass.add(addTypePanel);


        JPanel[] topPanel=batchGeneration.createJPanel(3);
        topPanel[0].setLayout(new GridLayout(1,1));
        topPanel[1].setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel topLabel=new JLabel("请输入信息");
        topPanel[1].add(topLabel);
        topPanel[0].add(topPanel[1]);
        topPanel[2].setLayout(new FlowLayout(FlowLayout.RIGHT));
        topPanel[0].add(topPanel[2]);
        addTypePanel.add(topPanel[0],BorderLayout.NORTH);//添加顶部到主窗口


        //设置中心主显示页面
        //设置垂直盒子
        JPanel centerPanel=new JPanel(new FlowLayout());
        Box centerBox=Box.createVerticalBox();
        centerPanel.add(centerBox);
        addTypePanel.add(centerPanel,BorderLayout.CENTER);

        JPanel[] infoTypeIn=batchGeneration.createJPanel(3);
        for(JPanel panel:infoTypeIn){
            panel.setLayout(new GridLayout(1,2));
        }

        JLabel[] infoLabel=batchGeneration.createJLabel(new String[]{"用户名","邮箱（必选）","创建日期","请输入密码","请再次输入密码"});
        JTextField[] infoField=batchGeneration.createJTextField(3,10);
        JPasswordField passwordField=new JPasswordField(10);
        JPasswordField passwordField1=new JPasswordField(10);
        JPanel[] passwordPanel=batchGeneration.createJPanel(2);
        for(JPanel panel:passwordPanel){
            panel.setLayout(new GridLayout(1,2));
        }

        for(int i=0;i<infoTypeIn.length;i++){
            infoTypeIn[i].add(infoLabel[i]);
            infoTypeIn[i].add(infoField[i]);
            centerBox.add(infoTypeIn[i]);
        }
        passwordPanel[0].add(infoLabel[3]);
        passwordPanel[0].add(passwordField);
        centerBox.add(passwordPanel[0]);
        passwordPanel[1].add(infoLabel[4]);
        passwordPanel[1].add(passwordField1);
        centerBox.add(passwordPanel[1]);

        passwordField.setEnabled(false);
        passwordField1.setEnabled(false);

        //底部窗口创建
        JPanel bottomArea=new JPanel(new FlowLayout());
        JButton[] southButton=batchGeneration.createJButton(new String[]{"确认账号","取消"});
        bottomArea.add(southButton[0]);
        bottomArea.add(southButton[1]);
        addTypePanel.add(bottomArea,BorderLayout.SOUTH);

        final String[] sql = new String[1];
        //创建账号按钮
        southButton[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (flag[0]) {
                    // 检查密码是否一致
                    if (!Arrays.equals(passwordField.getPassword(), passwordField1.getPassword())) {
                        jdpw.createInfoDialog(forgotPass, "两次输入的密码不一致", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                        return;
                    }
                    try {
                        String sql1 = "UPDATE user SET password = ? WHERE userID = ?";
                        PreparedStatement pre = con.prepareStatement(sql1);
                        pre.setString(1, new String(passwordField.getPassword()));
                        pre.setInt(2, userID[0]);
                        if (pre.executeUpdate() == 0) {
                            jdpw.createInfoDialog(forgotPass, "密码修改失败，请联系管理员解决问题", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                        } else {
                            jdpw.createInfoDialog(forgotPass, "密码修改成功!!", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                            main.login();
                            forgotPass.dispose();
                        }
                        pre.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    // 检查信息是否输入完整
                    if (infoField[0].getText().isEmpty() || infoField[1].getText().isEmpty() || infoField[2].getText().isEmpty()) {
                        jdpw.createInfoDialog(forgotPass, "请将信息输入完整", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                        return;
                    }
                    try {
                        String sql = "SELECT * FROM user WHERE userName = ? AND email = ? AND createData = ?";
                        PreparedStatement pre = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        pre.setString(1, infoField[0].getText());
                        pre.setString(2, infoField[1].getText());
                        pre.setString(3, infoField[2].getText());
                        ResultSet rs = pre.executeQuery();
                        if (!rs.next()) {
                            jdpw.createInfoDialog(forgotPass, "未找到用户", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                        } else {
                            rs.beforeFirst();
                            if (rs.next()) {
                                userID[0] = rs.getInt("userID");
                                jdpw.createInfoDialog(forgotPass, "成功找到账号，请重设密码", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                            }
                        }
                        pre.close();
                        passwordField.setEnabled(true);
                        passwordField1.setEnabled(true);
                        for (JTextField textField : infoField) {
                            textField.setEnabled(false);
                        }
                        southButton[0].setText("修改密码");
                        flag[0] = true;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        //取消按钮
        southButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forgotPass.dispose();
            }
        });
        forgotPass.pack();
        forgotPass.setLocationRelativeTo(null);
        forgotPass.setVisible(true);
    }












    public static void main(String[] args) throws SQLException {
        main.login(); // 获取初始登录状态

    }

}
