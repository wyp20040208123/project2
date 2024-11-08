package cn.popularSciencesManager.JFrame;

import cn.popularSciencesManager.model.*;
import cn.popularSciencesManager.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManagerFrame extends JFrame {
    public static void main(String[] args) throws SQLException {

        Main main=new Main(con,databaseName);
        main.login();
    }

    QueryBuilder queryBuilder=new QueryBuilder();

    JDialogPromptWindow jdpw=new JDialogPromptWindow();

    static String databaseName = "sciences";
    //2.获取数据库连接
    static String URL = "127.0.0.1:3306";
    static String url = "jdbc:mysql://" + URL + "/" + databaseName + "?serverTimezone=GMT%2B8  &  useSSL=true & characterEncoding=utf8";
    static String username = "root";
    static String password = "12345600O";
    static Connection con;

    static {
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    TableModelControl articlesSearch = new TableModelControl(TableModelControl.isCellEditable.UNCellEditable, con);

    TableModelControl articlesAlter = new TableModelControl(TableModelControl.isCellEditable.CellEditable, con);

    TableModelControl userSearch = new TableModelControl(TableModelControl.isCellEditable.UNCellEditable, con);

    TableModelControl userAlter = new TableModelControl(TableModelControl.isCellEditable.CellEditable, con);

    TableModelControl reviewSearch = new TableModelControl(TableModelControl.isCellEditable.UNCellEditable, con);

    TableModelControl articleTypeSearch = new TableModelControl(TableModelControl.isCellEditable.UNCellEditable, con);

    TableModelControl articleTypeAlter = new TableModelControl(TableModelControl.isCellEditable.CellEditable, con);

    TableModelControl favoriteSearch = new TableModelControl(TableModelControl.isCellEditable.UNCellEditable, con);

    ArticlesModel articlesModel = new ArticlesModel();

    ArticleTypeModel articleTypeModel = new ArticleTypeModel();

    FavoriteModel favoriteModel = new FavoriteModel();

    ReviewModel reviewModel = new ReviewModel();

    UserModel userModel = new UserModel();

    UserModel user=new UserModel();

    DbUtils dbUtils=new DbUtils(con,databaseName);

    static Map<String, String[]> databaseStruct;//数据表，用于存储表格与

    static private Boolean isLogin = false;
    BatchGeneration batchGeneration = new BatchGeneration();

    /***
     * 创建选项卡式窗体
     * @return 返回已创造号的选项卡式窗体
     */


    public ManagerFrame(UserModel user, Connection con) throws SQLException {
        this.user=user;
        setSize(800,600);
        this.setTitle(user.getUserType()+":"+user.getUserName());
        this.setLayout(new BorderLayout());

        databaseStruct=dbUtils.getDatabaseStruct();
        for (Map.Entry<String, String[]> entry : databaseStruct.entrySet()) {
            System.out.println("Table: " + entry.getKey());
            System.out.print("Columns: ");
            for (String column : entry.getValue()) {
                System.out.print(column + " ");
            }
            System.out.println();
        }


        //菜单分为两个
        //第一个：选项，内含重新连接数据库、选择数据表、退出
        //第二个：关于我
        JMenuBar menu = new JMenuBar();
        menu.setLayout(new FlowLayout(FlowLayout.LEFT));
        JMenuItem[] menuItem = batchGeneration.createJMenuItems(new String[]{"文章管理", "用户管理", "评论管理", "科普类别管理", "收藏管理"});
        for (int i = 0; i < menuItem.length; i++) {
            menu.add(menuItem[i]);
        }

        this.setJMenuBar(menu);

        //第一个选项卡：


        //创建选项板

        //文章相关选项
        String[] sTemp;
        sTemp = new String[]{"文章数据表", "文字信息修改", "发布文章"};
        JPanel[] articlesPanels = batchGeneration.createJPanel(sTemp.length);
        for (JPanel x : articlesPanels) {
            x.setLayout(new BorderLayout());
        }//写入布局管理器

        JTabbedPane articlesTabbedPane = batchGeneration.createJTabbedPane(sTemp, articlesPanels);//创建选项版


        //文章查询
        articlesPanels[0].add(articlesSearch.getTablePanel(), BorderLayout.CENTER);//获取组件


        JButton[] articlesSearchButton = batchGeneration.createJButton(new String[]{"查询文章信息", "查找信息", "删除全部记录", "删除被选记录", "转移到修改", "清空表格"});
        JPanel[] articlesSearchPanels = batchGeneration.createJPanel(5);
        articlesSearchPanels[0].setLayout(new GridLayout(2, 2));
        for (int i = 1; i < 5; i++) {
            articlesSearchPanels[i].setLayout(new FlowLayout(FlowLayout.LEFT));
            articlesSearchPanels[0].add(articlesSearchPanels[i]);
        }
        articlesSearchPanels[1].add(articlesSearchButton[0]);//查询全部信息
        articlesSearchPanels[1].add(articlesSearchButton[1]);//查找信息
        articlesSearchPanels[2].add(articlesSearchButton[2]);//删除全部记录
        articlesSearchPanels[2].add(articlesSearchButton[3]);//删除被选记录
        articlesSearchPanels[3].add(articlesSearchButton[4]);//转移到修改
        articlesSearchPanels[3].add(articlesSearchButton[5]);//清空表格
        articlesPanels[0].add(articlesSearchPanels[0], BorderLayout.SOUTH);
        articlesSearchButton[0].addActionListener(new ActionListener() {//查询全部信息
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                articlesSearch.clear();
                articlesSearch.loadTableData(queryBuilder
                        .select("articles.articleID")
                        .select("articles.authorID")
                        .select("user.userName")
                        .select("user.userType")
                        .select("articles.title")
                        .select("articles.createTime")
                        .select("articles.type")
                        .from("articles")
                        .from("user")
                        .where("articles.authorID = user.userID")
                        .build());

            }
        });

        articlesSearchButton[1].addActionListener(new ActionListener() {//查找信息
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                SelectDialog select=new SelectDialog(articlesModel,articleTypeSearch,databaseStruct,queryBuilder
                        .select("articles.articleID")
                        .select("articles.authorID")
                        .select("user.userName")
                        .select("user.userType")
                        .select("articles.title")
                        .select("articles.createTime")
                        .select("articles.type")
                        .from("articles")
                        .from("user")
                        .where("articles.authorID = user.userID")
                        .build());
                String sqls=select.init();
                System.out.println("sql="+sqls);
                articlesSearch.clear();
                articlesSearch.loadTableData(sqls);

            }
        });

        articlesSearchButton[2].addActionListener(new ActionListener() {//删除全部记录
            @Override
            public void actionPerformed(ActionEvent e) {
                 int result=deleteAllRows(articlesModel, articlesSearch);
                 if(result==-1){
                     jdpw.createInfoDialog(ManagerFrame.this,"删除失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                 }
                 else{
                     jdpw.createInfoDialog(ManagerFrame.this,"成功删除了"+result+"条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);

                 }
                queryBuilder = new QueryBuilder();
                articlesSearch.clear();
                articlesSearch.loadTableData(queryBuilder
                        .select("articles.articleID")
                        .select("articles.authorID")
                        .select("user.userName")
                        .select("user.userType")
                        .select("articles.title")
                        .select("articles.createTime")
                        .select("articles.type")
                        .from("articles")
                        .from("user")
                        .where("articles.authorID = user.userID")
                        .build());


            }
        });

        articlesSearchButton[3].addActionListener(new ActionListener() {//删除被选记录
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> sql=deleteSelectRows(articlesModel, articlesSearch);
                if(sql==null)
                    return;

                int sum = 0;
                for (String x : sql) {
                    try {
                        Statement stmt = con.createStatement();
                        sum += stmt.executeUpdate(x);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }


                if (sum == 0) {
                    jdpw.createInfoDialog(ManagerFrame.this,"删除操作执行失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                else{
                    jdpw.createInfoDialog(ManagerFrame.this,"成功删除了"+sum+"条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);

                }

                queryBuilder = new QueryBuilder();
                articlesSearch.clear();
                articlesSearch.loadTableData(queryBuilder
                        .select("articles.articleID")
                        .select("articles.authorID")
                        .select("user.userName")
                        .select("user.userType")
                        .select("articles.title")
                        .select("articles.createTime")
                        .select("articles.type")
                        .from("articles")
                        .from("user")
                        .where("articles.authorID = user.userID")
                        .build());
            }
        });

        //转移到修改
        articlesSearchButton[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                articlesAlter.clear();
                articlesAlter.loadTableData(AlterSelectRows(articlesModel,articlesSearch));
            }
        });

        articlesSearchButton[5].addActionListener(new ActionListener() {//清空表格
            @Override
            public void actionPerformed(ActionEvent e) {
                articlesSearch.clear();
            }
        });


        //文章信息修改
        articlesPanels[1].add(articlesAlter.getTablePanel(), BorderLayout.CENTER);//获取组件
        JFrameModel articlesAlterModel=new JFrameModel();
        articlesPanels[1].add(articlesAlterModel.getAlterPanel(articlesModel,articlesAlter),BorderLayout.SOUTH);


        //文章发布

        //顶部区域
        JPanel articleAddTopPanel=new JPanel(new FlowLayout());
        articleAddTopPanel.add(new JLabel("发布文章"));


        //中心区域
        JPanel articleAddCenterBox=new JPanel(new BorderLayout());//设置垂直分布的BOX；
        //第一层：板块选择
        JPanel[] articleAddGetInfo=batchGeneration.createJPanel(3);
        articleAddGetInfo[0].setLayout(new GridLayout(1,2));
        articleAddGetInfo[1].setLayout(new FlowLayout(FlowLayout.LEFT));
        articleAddGetInfo[2].setLayout(new FlowLayout(FlowLayout.RIGHT));
        articleAddGetInfo[0].add(articleAddGetInfo[1]);
        articleAddGetInfo[0].add(articleAddGetInfo[2]);
        JLabel[] articleAddGetInfoLabel= batchGeneration.createJLabel(new String[]{"文章标题:","发布板块"});
        JTextField articleAddGetTitle=new JTextField(10);
        List<String> articleTypeName=new ArrayList<>();
        try{
           String sql="SELECT type from articletype";
           PreparedStatement pr= con.prepareStatement(sql);
           ResultSet rs=pr.executeQuery();
           while(rs.next()){
               articleTypeName.add(rs.getString("type"));
           }
        }catch (SQLException e){
            e.printStackTrace();
        }
        JComboBox<String> articleAddGetTypeComboBox=new JComboBox<>(ObjectArryToStringArray(articleTypeName.toArray()));//创建板块选择框
        articleAddGetInfo[1].add(articleAddGetInfoLabel[0]);
        articleAddGetInfo[1].add(articleAddGetTitle);
        articleAddGetInfo[2].add(articleAddGetInfoLabel[1]);
        articleAddGetInfo[2].add(articleAddGetTypeComboBox);
        articleAddCenterBox.add(articleAddGetInfo[0],BorderLayout.NORTH);
        //中心第二层：内容获取
        JTextArea articleGetArea=new JTextArea();//创建输入文本框
        JScrollPane articleAddScroll=new JScrollPane(articleGetArea);
        articleAddCenterBox.add(articleAddScroll,BorderLayout.CENTER);
        //第三层：额外功能
        JButton articleGet=new JButton("载入现有文章");
        JPanel articleGetPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        articleGetPanel.add(articleGet);
        articleAddCenterBox.add(articleGetPanel,BorderLayout.SOUTH);

        //底部窗口
        JPanel[] articleBottomPanel= batchGeneration.createJPanel(3);
        articleBottomPanel[0].setLayout(new GridLayout(1,2));
        articleBottomPanel[0].setLayout(new FlowLayout());
        articleBottomPanel[0].setLayout(new FlowLayout());
        articleBottomPanel[0].add(articleBottomPanel[1]);
        articleBottomPanel[0].add(articleBottomPanel[2]);//设置基本布局
        JButton[] articleBottomButton= batchGeneration.createJButton(new String[]{"发布","清空"});
        articleBottomPanel[1].add(articleBottomButton[0]);
        articleBottomPanel[2].add(articleBottomButton[1]);//设置按钮


        //顶部主窗体添加
        articlesPanels[2].add(articleAddTopPanel,BorderLayout.NORTH);
        //中心区域主窗体添加
        articlesPanels[2].add(new JPanel(),BorderLayout.EAST);
        articlesPanels[2].add(new JPanel(),BorderLayout.WEST);
        articlesPanels[2].add(articleAddCenterBox, BorderLayout.CENTER);
        //底部主体窗口添加
        articlesPanels[2].add(articleBottomPanel[0],BorderLayout.SOUTH);


        //载入现有文章
        articleGet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jf=new JFileChooser();
                jf.showOpenDialog(null);
                jf.setVisible(true);
                File file=jf.getSelectedFile();//获取文件
                String text = null;
                try {
                    text = Files.readString(file.toPath());//读取文件
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // 创建 JTextArea 并设置内容
                articleGetArea.setText(text);//保存文件
            }
        });

        //发布
        articleBottomButton[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(articleAddGetTitle.getText().isEmpty()){
                    jdpw.createInfoDialog(ManagerFrame.this,"请输入标题", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                try{
                    String sql="INSERT INTO articles VALUES( ? , ? , ? , ? , ? , ?)";
                    PreparedStatement pr= con.prepareStatement(sql);
                    pr.setNull(1,Types.INTEGER);
                    pr.setInt(2,user.getUserID());
                    pr.setString(3,articleAddGetTitle.getText());
                    pr.setString(4,articleGetArea.getText());
                    pr.setTimestamp(5,new Timestamp(System.currentTimeMillis()));
                    pr.setString(6,articleAddGetTypeComboBox.getSelectedItem().toString());
                    if(pr.executeUpdate()==0){
                        jdpw.createInfoDialog(ManagerFrame.this,"发布文章失败，请重试", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                    }
                    else {
                        jdpw.createInfoDialog(ManagerFrame.this,"文章发表成功", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                        articleAddGetTitle.setText(null);
                        articleGetArea.setText(null);
                    }
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
            }
        });

        //清空
        articleBottomButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                articleGetArea.setText(null);
                articleAddGetTitle.setText(null);
            }
        });

        //用户相关选项

        sTemp = new String[]{"用户数据表", "修改用户信息"};
        JPanel[] userPanels = batchGeneration.createJPanel(sTemp.length);
        for (JPanel x : userPanels) {
            x.setLayout(new BorderLayout());
        }

        JTabbedPane userTabbedPane = batchGeneration.createJTabbedPane(sTemp, userPanels);


        //用户数据表
        userPanels[0].add(userSearch.getTablePanel(), BorderLayout.CENTER);//获取组件


        JButton[] userSearchButton = batchGeneration.createJButton(new String[]{"查询用户信息", "查找信息", "删除全部记录", "删除被选记录", "转移到修改", "清空表格","增加用户"});
        JPanel[] userSearchPanels = batchGeneration.createJPanel(5);
        userSearchPanels[0].setLayout(new GridLayout(2, 2));
        for (int i = 1; i < 5; i++) {
            userSearchPanels[i].setLayout(new FlowLayout(FlowLayout.LEFT));
            userSearchPanels[0].add(userSearchPanels[i]);
        }
        userSearchPanels[1].add(userSearchButton[0]);//查询全部信息
        userSearchPanels[1].add(userSearchButton[1]);//查找信息
        userSearchPanels[2].add(userSearchButton[2]);//删除全部记录
        userSearchPanels[2].add(userSearchButton[3]);//删除被选记录
        userSearchPanels[3].add(userSearchButton[4]);//转移到修改
        userSearchPanels[3].add(userSearchButton[5]);//清空表格
        userSearchPanels[4].add(userSearchButton[6]);//增加用户

        userPanels[0].add(userSearchPanels[0], BorderLayout.SOUTH);

        userSearchButton[0].addActionListener(new ActionListener() {//查询全部信息
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                userSearch.clear();
                userSearch.loadTableData(queryBuilder.select("*").from("user").build());
            }
        });

        //查找信息
        userSearchButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                SelectDialog select=new SelectDialog(userModel,userSearch,databaseStruct,queryBuilder
                        .select("*").from("user").where("user.userID = user.userID").build());
                userSearch.clear();
                userSearch.loadTableData(select.init());
            }
        });

        //删除全部记录
        userSearchButton[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result=deleteAllRows(userModel, userSearch);
                if(result==-1){
                    jdpw.createInfoDialog(ManagerFrame.this,"删除失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                else{
                    jdpw.createInfoDialog(ManagerFrame.this,String.format("成功删除了%d条记录",result), JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);

                }
                queryBuilder = new QueryBuilder();
                userSearch.clear();
                userSearch.loadTableData(queryBuilder.select("*").from("user").build());
            }
        });

        //删除被选记录
        userSearchButton[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> sql = deleteSelectRows(userModel, userSearch);
                if(sql==null)
                    return;
                int sum = 0;
                for (String x : sql) {
                    try {
                        Statement stmt = con.createStatement();
                        sum += stmt.executeUpdate(x);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }


                if (sum == 0) {
                    jdpw.createInfoDialog(ManagerFrame.this,"删除操作执行失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                } else {
                    jdpw.createInfoDialog(ManagerFrame.this,"已成功删除" + sum + "条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }

                queryBuilder = new QueryBuilder();
                userSearch.clear();
                userSearch.loadTableData(queryBuilder.select("*").from("user").build());
            }

        });

        //转移到修改
        userSearchButton[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userAlter.clear();
                userAlter.loadTableData(AlterSelectRows(userModel,userSearch));
            }
        });

        //清空表格
        userSearchButton[5].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userSearch.clear();
            }
        });

        //增加用户
        userSearchButton[6].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog addType=new JDialog(ManagerFrame.this);
                addType.setTitle("添加新用户");
                addType.setLayout(new FlowLayout());
                JPanel addTypePanel=new JPanel(new BorderLayout());
                addType.add(addTypePanel);


                JPanel[] topPanel=batchGeneration.createJPanel(3);
                topPanel[0].setLayout(new GridLayout(1,2));
                topPanel[1].setLayout(new FlowLayout(FlowLayout.LEFT));
                JLabel topLabel=new JLabel("请输入新用户的信息");
                topPanel[1].add(topLabel);
                topPanel[0].add(topPanel[1]);
                topPanel[2].setLayout(new FlowLayout(FlowLayout.RIGHT));
                JButton topButton=new JButton("增加列");
                topPanel[2].add(topButton);
                topPanel[0].add(topPanel[2]);
                addTypePanel.add(topPanel[0],BorderLayout.NORTH);//添加顶部到主窗口


                //设置中心主显示页面
                //设置垂直盒子
                Box centerBox=Box.createVerticalBox();
                addTypePanel.add(centerBox,BorderLayout.CENTER);

                //提示面板创建
                JPanel cueColumn =new JPanel(new GridLayout(1,4));
                JLabel[] cueColumnLabel=batchGeneration.createJLabel(new String[]{ "用户名","密码","email","用户类型"});

                for(JLabel label:cueColumnLabel){
                    cueColumn.add(label);
                }
                centerBox.add(cueColumn);


                List<addBox> addBoxes=new ArrayList<>();
                addBoxes.add(new addBox());
                centerBox.add(addBoxes.get(0).getPanel());


                //底部窗口创建
                JPanel bottomArea=new JPanel(new FlowLayout());
                JButton[] southButton=batchGeneration.createJButton(new String[]{"确认","取消"});
                bottomArea.add(southButton[0]);
                bottomArea.add(southButton[1]);
                addTypePanel.add(bottomArea,BorderLayout.SOUTH);



                //添加行
                topButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addBoxes.add(new addBox());
                        centerBox.add(addBoxes.get(addBoxes.size()-1).getPanel());
                        addType.validate();
                        addType.repaint();
                        addType.pack();
                    }
                });
                //确认按钮
                southButton[0].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String sql;
                        int sum=0;
                        int fail=0;
                        for(addBox box:addBoxes){
                            //当判定为不能被插入时，
                            if(!box.CheckTrue()){
                                fail++;
                                continue;
                            }
                            try{
                                sql = "INSERT INTO user VALUES (?, ?, ?, ?, ?, ?)";
                                PreparedStatement pre = con.prepareStatement(sql);
                                pre.setNull(1, Types.INTEGER); // 用户ID系统自动生成
                                pre.setString(2, box.getTextField(0).getText()); // 用户名
                                pre.setDate(3, Date.valueOf(LocalDate.now())); // 日期
                                pre.setString(4, box.getTextField(1).getText()); // 用户密码

                                if (box.getTextField(2).getText().isEmpty()) {
                                    pre.setNull(5, Types.VARCHAR); // 用户email
                                } else {
                                    pre.setString(5, box.getTextField(2).getText());
                                }

                                pre.setString(6, (String) box.getUserTypeBox().getSelectedItem());

                                sum+= pre.executeUpdate();
                                pre.close();
                            }catch (SQLException ex){
                                ex.printStackTrace();
                            }

                        }
                        if (sum==0 && fail==0){
                            jdpw.createInfoDialog(addType,"插入失败\n可能的原因有：用户名/密码存在未输入的情况", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                        }
                        else if(sum==0 && fail!=0 ){
                            jdpw.createInfoDialog(addType,"插入失败\n请检查用户名/密码输入情况", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                        }
                        else {
                            jdpw.createInfoDialog(addType,String.format("成功插入%d条记录\n插入失败%d条记录",sum,fail), JDialogPromptWindow.DialogSelect.CLOSED_WINDOWS);
                            return;
                        }
                    }
                });

                //取消按钮
                southButton[1].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addType.dispose();
                    }
                });
                addType.pack();
                addType.setModal(true);
                addType.setLocationRelativeTo(null);
                addType.setVisible(true);
            }
            class addBox{
                JPanel panel=new JPanel(new FlowLayout());
                JTextField[] textField=batchGeneration.createJTextField(3,5);
                String[] userType={"用户","管理员"};
                JComboBox<String> userTypeBox=new JComboBox<>(userType);

                addBox(){
                    for(JTextField textField:textField)
                        panel.add(textField);
                    panel.add(userTypeBox);
                }


                public JPanel getPanel() {
                    return panel;
                }

                public JTextField getTextField(int index) {
                    return textField[index];
                }

                public JComboBox<String> getUserTypeBox() {
                    return userTypeBox;
                }

                public Boolean CheckTrue(){//判断是否可以继续
                    if(textField[0].getText().isEmpty()||textField[1].getText().isEmpty()){
                        return false;
                    }
                    else return true;
                }
            }
        });




        //修改用户信息
        userPanels[1].add(userAlter.getTablePanel(), BorderLayout.CENTER);
        JFrameModel userAlterModel=new JFrameModel();
        userPanels[1].add(userAlterModel.getAlterPanel(userModel,userAlter),BorderLayout.SOUTH);




        //评论相关选项
        sTemp = new String[]{"查找评论"};
        JPanel[] reviewPanels = batchGeneration.createJPanel(sTemp.length);
        for (JPanel x : reviewPanels) {
            x.setLayout(new BorderLayout());
        }
        JTabbedPane reviewTabbedPane = batchGeneration.createJTabbedPane(sTemp, reviewPanels);


        //查找评论
        reviewPanels[0].add(reviewSearch.getTablePanel(), BorderLayout.CENTER);

        JButton[] reviewSearchButton = batchGeneration.createJButton(new String[]{"查询评论信息", "查找信息", "删除全部记录", "删除被选记录", "清空表格"});
        JPanel[] reviewSearchPanels = batchGeneration.createJPanel(5);
        reviewSearchPanels[0].setLayout(new GridLayout(2, 2));
        for (int i = 1; i < 5; i++) {
            reviewSearchPanels[i].setLayout(new FlowLayout(FlowLayout.LEFT));
            reviewSearchPanels[0].add(reviewSearchPanels[i]);
        }
        reviewSearchPanels[1].add(reviewSearchButton[0]);//查询全部信息
        reviewSearchPanels[1].add(reviewSearchButton[1]);//查找信息
        reviewSearchPanels[2].add(reviewSearchButton[2]);//删除全部记录
        reviewSearchPanels[2].add(reviewSearchButton[3]);//删除部分记录
        reviewSearchPanels[3].add(reviewSearchButton[4]);//清空表格
        reviewPanels[0].add(reviewSearchPanels[0], BorderLayout.SOUTH);

        reviewSearchButton[0].addActionListener(new ActionListener() {//查询全部信息
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                reviewSearch.clear();
                reviewSearch.loadTableData(queryBuilder
                        .select("review.articleID")
                        .select("articles.title")
                        .select("review.review")
                        .select("review.userID")
                        .select("user.userName")
                        .select("user.userType")
                        .select("review.createTime")
                        .select("review.likeNum")
                        .select("articles.type")
                        .from("articles")
                        .from("user")
                        .from("review")
                        .where("review.userID = user.userID")
                        .where("review.articleID = articles.articleID")
                        .build());
            }
        });


        //查找信息
        reviewSearchButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                SelectDialog select=new SelectDialog(reviewModel,reviewSearch,databaseStruct,queryBuilder
                        .select("review.articleID")
                        .select("articles.title")
                        .select("review.review")
                        .select("review.userID")
                        .select("user.userName")
                        .select("user.userType")
                        .select("review.createTime")
                        .select("review.likeNum")
                        .select("articles.type")
                        .from("articles")
                        .from("user")
                        .from("review")
                        .where("review.userID = user.userID")
                        .where("review.articleID = articles.articleID")
                        .build()+" AND ");
                reviewSearch.clear();
                reviewSearch.loadTableData(select.init());
            }
        });


        //删除全部记录
        reviewSearchButton[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result=deleteAllRows(reviewModel,reviewSearch);
                if(result==-1){
                    jdpw.createInfoDialog(ManagerFrame.this,"删除失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                else{
                    jdpw.createInfoDialog(ManagerFrame.this,"成功删除了"+result+"条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                queryBuilder = new QueryBuilder();
                reviewSearch.clear();
                reviewSearch.loadTableData(queryBuilder
                        .select("review.articleID")
                        .select("articles.title")
                        .select("review.review")
                        .select("review.userID")
                        .select("user.userName")
                        .select("user.userType")
                        .select("review.createTime")
                        .select("review.likeNum")
                        .select("articles.type")
                        .from("articles")
                        .from("user")
                        .from("review")
                        .where("review.userID = user.userID")
                        .where("review.articleID = articles.articleID")
                        .build());

            }
        });


        //删除被选记录
        reviewSearchButton[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> sql = deleteSelectRows(reviewModel, reviewSearch);
                if(sql==null)
                    return;

                int sum = 0;
                for (String x : sql) {
                    try {
                        Statement stmt = con.createStatement();
                        sum += stmt.executeUpdate(x);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                if (sum == 0) {
                    jdpw.createInfoDialog(ManagerFrame.this,"删除操作执行失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                } else {
                    jdpw.createInfoDialog(ManagerFrame.this,"已成功删除"+ sum + "条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                queryBuilder = new QueryBuilder();
                reviewSearch.clear();
                reviewSearch.loadTableData(queryBuilder
                        .select("review.articleID")
                        .select("articles.title")
                        .select("review.userID")
                        .select("user.userName")
                        .select("user.userType")
                        .select("review.createTime")
                        .select("review.likeNum")
                        .select("articles.type")
                        .from("articles")
                        .from("user")
                        .from("review")
                        .where("review.userID = user.userID")
                        .where("review.articleID = articles.articleID")
                        .build());

            }
        });



        //清空表格
        reviewSearchButton[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reviewSearch.clear();
            }
        });


        //文章类别管理
        sTemp = new String[]{"科普类别数据表", "修改科普类别"};
        JPanel[] articleTypePanels = batchGeneration.createJPanel(sTemp.length);
        for (JPanel x : articleTypePanels) {
            x.setLayout(new BorderLayout());
        }
        JTabbedPane articleTypeTabbedPane = batchGeneration.createJTabbedPane(sTemp, articleTypePanels);

        //科普类别数据表
        articleTypePanels[0].add(articleTypeSearch.getTablePanel(), BorderLayout.CENTER);

        JButton[] articleTypeSearchButton = batchGeneration.createJButton(new String[]{"查询全部信息", "查找信息", "删除全部记录", "删除被选记录", "转移到修改", "清空表格","增加科普类别"});
        JPanel[] articleTypeSearchPanels = batchGeneration.createJPanel(5);
        articleTypeSearchPanels[0].setLayout(new GridLayout(2, 2));
        for (int i = 1; i < 5; i++) {
            articleTypeSearchPanels[i].setLayout(new FlowLayout(FlowLayout.LEFT));
            articleTypeSearchPanels[0].add(articleTypeSearchPanels[i]);
        }
        articleTypeSearchPanels[1].add(articleTypeSearchButton[0]);//查询全部信息
        articleTypeSearchPanels[1].add(articleTypeSearchButton[1]);//查找信息
        articleTypeSearchPanels[2].add(articleTypeSearchButton[2]);//删除全部记录
        articleTypeSearchPanels[2].add(articleTypeSearchButton[3]);//删除被选记录
        articleTypeSearchPanels[3].add(articleTypeSearchButton[4]);//转移到修改
        articleTypeSearchPanels[3].add(articleTypeSearchButton[5]);//清空表格
        articleTypeSearchPanels[4].add(articleTypeSearchButton[6]);//增加科普类别
        articleTypePanels[0].add(articleTypeSearchPanels[0], BorderLayout.SOUTH);

        articleTypeSearchButton[0].addActionListener(new ActionListener() {//查询全部信息
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                articleTypeSearch.clear();
                articleTypeSearch.loadTableData(queryBuilder.select("*").from("articletype").build());
            }
        });

        //查找信息
        articleTypeSearchButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                articleTypeSearch.clear();
                SelectDialog select=new SelectDialog(articleTypeModel,articleTypeSearch,databaseStruct,queryBuilder
                        .select("*").from("articletype").where("type=type").build()+" AND ");
                articleTypeSearch.loadTableData(select.init());

            }
        });


        //删除全部记录
        articleTypeSearchButton[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result=deleteAllRows(articleTypeModel,articleTypeSearch);
                if(result==-1){
                    jdpw.createInfoDialog(ManagerFrame.this,"删除失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                else{
                    jdpw.createInfoDialog(ManagerFrame.this,"成功删除了"+result+"条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                queryBuilder = new QueryBuilder();
                articleTypeSearch.clear();
                articleTypeSearch.loadTableData(queryBuilder.select("*").from("articletype").build());
            }
        });


        //删除被选记录
        articleTypeSearchButton[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> sql = deleteSelectRows(articleTypeModel, articleTypeSearch);
                if(sql==null)
                    return;
                int sum = 0;
                for (String x : sql) {
                    try {
                        Statement stmt = con.createStatement();
                        sum += stmt.executeUpdate(x);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                if (sum == 0) {
                    jdpw.createInfoDialog(ManagerFrame.this,"删除操作执行失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                } else {
                    jdpw.createInfoDialog(ManagerFrame.this,"成功删除了"+sum+"条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }


                queryBuilder = new QueryBuilder();
                articleTypeSearch.loadTableData(queryBuilder.from("articletype").build());

            }
        });


        //转移到修改
        articleTypeSearchButton[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                articleTypeAlter.loadTableData(AlterSelectRows(articleTypeModel,articleTypeSearch));
            }
        });

        //清空表格
        articleTypeSearchButton[5].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                articleTypeSearch.clear();
            }
        });

        //增加科普类别
        articleTypeSearchButton[6].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog addType=new JDialog(ManagerFrame.this);
                addType.setTitle("添加科普类别");
                addType.setLayout(new FlowLayout());
                JPanel addTypePanel=new JPanel(new BorderLayout());
                addType.add(addTypePanel);


                JPanel[] topPanel=batchGeneration.createJPanel(3);
                topPanel[0].setLayout(new GridLayout(1,2));
                topPanel[1].setLayout(new FlowLayout(FlowLayout.LEFT));
                JLabel topLabel=new JLabel("请输入要增加的科普类别");
                topPanel[1].add(topLabel);
                topPanel[0].add(topPanel[1]);
                topPanel[2].setLayout(new FlowLayout(FlowLayout.RIGHT));
                JButton topButton=new JButton("增加列");
                topPanel[2].add(topButton);
                topPanel[0].add(topPanel[2]);
                addTypePanel.add(topPanel[0],BorderLayout.NORTH);//添加顶部到主窗口


                //设置中心主显示页面
                //设置垂直盒子
                Box centerBox=Box.createVerticalBox();
                addTypePanel.add(centerBox,BorderLayout.CENTER);

                //提示面板创建

                JLabel cueLabel=new JLabel("要添加的类");
                centerBox.add(cueLabel);
                List<addBox> addBoxes=new ArrayList<>();
                addBoxes.add(new addBox());
                centerBox.add(addBoxes.get(0).getPanel());
                //底部窗口创建
                JPanel bottomArea=new JPanel(new FlowLayout());
                JButton[] southButton=batchGeneration.createJButton(new String[]{"确认","取消"});
                bottomArea.add(southButton[0]);
                bottomArea.add(southButton[1]);
                addTypePanel.add(bottomArea,BorderLayout.SOUTH);



                //添加行
                topButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addBoxes.add(new addBox());
                        centerBox.add(addBoxes.get(addBoxes.size()-1).getPanel());
                        addType.validate();
                        addType.repaint();
                        addType.pack();
                    }
                });
                List<String> sql=new ArrayList<>();
                //确认按钮
                southButton[0].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String temp=null;
                        for(addBox box:addBoxes){
                            temp=box.getTypeInText();
                            if(temp.isEmpty())
                                continue;
                            temp="INSERT INTO articletype VALUES (\""+temp+"\")";
                            sql.add(temp);
                        }
                        if (temp==null){
                            jdpw.createInfoDialog(addType,"插入失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                            return;
                        }
                        else{
                            int sum=0;
                            try {
                                for(String x:sql) {
                                    System.out.println(sql);
                                    PreparedStatement pre=con.prepareStatement(x);
                                    sum+=pre.executeUpdate();
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                            finally {
                                jdpw.createInfoDialog(addType,String.format("成功添加%d条记录",sum), JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                                addType.dispose();
                                return;
                            }
                        }
                    }
                });

                //取消按钮
                southButton[1].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addType.dispose();
                    }
                });
                addType.pack();
                addType.setModal(true);
                addType.setLocationRelativeTo(null);
                addType.setVisible(true);
            }
            class addBox{
                JPanel panel=new JPanel(new FlowLayout());
                JLabel label=new JLabel("新类别");
                JTextField textField=new JTextField(8);

                addBox(){
                    panel.add(label);
                    panel.add(textField);
                }

                public JTextField getTextField() {
                    return textField;
                }

                public JPanel getPanel() {
                    return panel;
                }

                public String getTypeInText(){
                    if(textField.getText().isEmpty()){
                        return null;
                    }
                    else return textField.getText();
                }
            }
        });

        //修改科普类别
        articleTypePanels[1].add(articleTypeAlter.getTablePanel(), BorderLayout.CENTER);
        JFrameModel articleTypeAlterModel=new JFrameModel();
        articleTypePanels[1].add(articleTypeAlterModel.getAlterPanel(articleTypeModel,articleTypeAlter),BorderLayout.SOUTH);


        //收藏夹管理
        sTemp = new String[]{"收藏数据表"};
        JPanel[] favoritePanels = batchGeneration.createJPanel(sTemp.length);
        for (JPanel x : favoritePanels) {
            x.setLayout(new BorderLayout());
        }
        JTabbedPane favoriteTabbedPane = batchGeneration.createJTabbedPane(sTemp, favoritePanels);

        //收藏数据表
        favoritePanels[0].add(favoriteSearch.getTablePanel(), BorderLayout.CENTER);

        JButton[] favoriteSearchButton = batchGeneration.createJButton(new String[]{"查询全部信息", "查找信息", "删除全部记录", "删除被选记录", "清空表格"});
        JPanel[] favoriteSearchPanels = batchGeneration.createJPanel(5);
        favoriteSearchPanels[0].setLayout(new GridLayout(2, 2));
        for (int i = 1; i < 5; i++) {
            favoriteSearchPanels[i].setLayout(new FlowLayout(FlowLayout.LEFT));
            favoriteSearchPanels[0].add(favoriteSearchPanels[i]);
        }
        favoriteSearchPanels[1].add(favoriteSearchButton[0]);//查询全部信息
        favoriteSearchPanels[1].add(favoriteSearchButton[1]);//查找信息
        favoriteSearchPanels[2].add(favoriteSearchButton[2]);//删除全部记录
        favoriteSearchPanels[2].add(favoriteSearchButton[3]);//删除被选记录
        favoriteSearchPanels[3].add(favoriteSearchButton[4]);//清空表格
        favoritePanels[0].add(favoriteSearchPanels[0], BorderLayout.SOUTH);

        favoriteSearchButton[0].addActionListener(new ActionListener() {//查询全部信息
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                favoriteSearch.clear();
                favoriteSearch.loadTableData(queryBuilder
                        .select("user.*")
                        .select("articles.*")
                        .from("user")
                        .from("articles")
                        .from("favorite")
                        .where("favorite.userID = user.userID")
                        .where("favorite.articleID = articles.articleID")
                        .build()
                );
            }
        });

        //查找信息
        favoriteSearchButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                SelectDialog select=new SelectDialog(favoriteModel,favoriteSearch,databaseStruct,queryBuilder
                        .select("user.*")
                        .select("articles.*")
                        .from("user")
                        .from("articles")
                        .from("favorite")
                        .where("favorite.userID = user.userID")
                        .where("favorite.articleID = articles.articleID")
                        .build());
                favoriteSearch.loadTableData(select.init());
            }
        });

        //删除全部记录
        favoriteSearchButton[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result=deleteAllRows(favoriteModel,favoriteSearch);
                if(result==-1){
                    jdpw.createInfoDialog(ManagerFrame.this,"删除失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                else{
                    jdpw.createInfoDialog(ManagerFrame.this,"成功删除了"+result+"条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                queryBuilder = new QueryBuilder();
                favoriteSearch.clear();
                favoriteSearch.loadTableData(queryBuilder
                        .select("user.*")
                        .select("articles.*")
                        .from("user")
                        .from("articles")
                        .from("favorite")
                        .where("favorite.userID = user.userID")
                        .where("favorite.articleID = articles.articleID")
                        .build()
                );
            }
        });

        //删除被选记录
        favoriteSearchButton[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> sql = deleteSelectRows(favoriteModel, favoriteSearch);
                if (sql==null)
                    return;
                int sum = 0;
                for (String x : sql) {
                    try {
                        Statement stmt = con.createStatement();
                        sum += stmt.executeUpdate(x);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }


                if (sum == 0) {
                    jdpw.createInfoDialog(ManagerFrame.this,"删除操作执行失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                else{
                    jdpw.createInfoDialog(ManagerFrame.this,"成功删除了"+sum+"条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);

                }

                queryBuilder = new QueryBuilder();
                favoriteSearch.clear();
                favoriteSearch.loadTableData(queryBuilder
                        .select("*")
                        .from("user")
                        .from("articles")
                        .from("favorite")
                        .where("favorite.userID = user.userID")
                        .where("favorite.articleID = articles.articleID")
                        .build()
                );


            }
        });

        //清空表格
        favoriteSearchButton[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                favoriteSearch.clear();
            }
        });


        //文章管理
        menuItem[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagerFrame.this.getContentPane().removeAll();
                ManagerFrame.this.add(articlesTabbedPane, BorderLayout.CENTER);
                ManagerFrame.this.validate();
                ManagerFrame.this.repaint();
            }
        });

        //用户管理
        menuItem[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagerFrame.this.getContentPane().removeAll();
                ManagerFrame.this.add(userTabbedPane, BorderLayout.CENTER);
                ManagerFrame.this.validate();
                ManagerFrame.this.repaint();
            }
        });

        //评论管理
        menuItem[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagerFrame.this.getContentPane().removeAll();
                ManagerFrame.this.add(reviewTabbedPane, BorderLayout.CENTER);
                ManagerFrame.this.validate();
                ManagerFrame.this.repaint();
            }
        });

        //科普类别管理
        menuItem[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagerFrame.this.getContentPane().removeAll();
                ManagerFrame.this.add(articleTypeTabbedPane, BorderLayout.CENTER);
                ManagerFrame.this.validate();
                ManagerFrame.this.repaint();
            }
        });

        //收藏管理
        menuItem[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagerFrame.this.getContentPane().removeAll();
                ManagerFrame.this.add(favoriteTabbedPane, BorderLayout.CENTER);
                ManagerFrame.this.validate();
                ManagerFrame.this.repaint();
            }
        });



        setLocationRelativeTo(null);//设置居中
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }






    private int[] pKeyCheck(String[] primaryKeys, String[] col) {
        int[] pkColumnNum = new int[primaryKeys.length];//记录
        int i;
        for (i = 0; i < primaryKeys.length; i++) {
            pkColumnNum[i] = -1;
            int j = 0;
            for (String x : col) {
                if (x.equals(primaryKeys[i])) {
                    pkColumnNum[i] = j;
                    break;
                }
                j++;
            }
            if (pkColumnNum[i] == -1) break;
        }//匹配主键，并获取其在表格中的位置


        if (i != primaryKeys.length) {
            System.out.println("未找到主键");
            return null;
        }
        return pkColumnNum;
    }

    private List<String> deleteSelectRows(AllModel model, TableModelControl table) {
        List<String> sqls = new ArrayList<>();
        String[] col = ObjectArryToStringArray(table.getColumnNames());
        Object[][] row = table.getRows();
        String[] pk = model.getPrimaryKey();
        int[] pkColumnNum = pKeyCheck(pk, col);//记录

        if (pkColumnNum == null) {
            return null;
        }
        int[] selectedRows = table.getTable().getSelectedRows();


        for (int j = selectedRows.length - 1; j >= 0; j--) {
            StringBuilder sql = new StringBuilder("Delete From ");
            sql.append(model.getDatabaseName()).append(" where ");
            sql.append(pk[0]).append(" = \"").append(row[selectedRows[j]][pkColumnNum[0]]).append("\"");
            for (int i = 1; i < pk.length; i++) {
                sql.append(" and ").append(pk[i]).append(" = \"").append(row[selectedRows[j]][pkColumnNum[i]]).append("\"");
            }
            sqls.add(sql.toString());
        }
        return sqls;
    }

    private String AlterSelectRows(AllModel model, TableModelControl table) {
        StringBuilder sql;
        String[] col = ObjectArryToStringArray(table.getColumnNames());
        Object[][] row = table.getRows();
        String[] pk = model.getPrimaryKey();
        int[] pkColumnNum = pKeyCheck(pk, col);//记录

        if (pkColumnNum == null) {
            return null;
        }
        int[] selectedRows = table.getTable().getSelectedRows();
        sql = new StringBuilder("Select * FROM ");
        sql.append(model.getDatabaseName()).append(" where ");
        for (int j = selectedRows.length - 1; j >= 0; j--) {
            sql.append(pk[0]).append(" = \"").append(row[selectedRows[j]][pkColumnNum[0]]).append("\"");
            for (int i = 1; i < pk.length; i++) {
                sql.append(" and ").append(pk[i]).append(" = \"").append(row[selectedRows[j]][pkColumnNum[i]]).append("\"");
            }
            sql.append(" or ");

        }
        sql.delete(sql.length()-4,sql.length()-1);
        System.out.println(sql);
        return sql.toString();
    }

    private int deleteAllRows(AllModel model, TableModelControl table) {
        List<String> sqls = new ArrayList<>();
        String[] col = ObjectArryToStringArray(table.getColumnNames());//将object转换为String
        if(col==null) return -1;
        Object[][] row = table.getRows();
        String[] pk = model.getPrimaryKey();
        int[] pkColumnNum = pKeyCheck(pk, col);//记录

        if (pkColumnNum == null) {
            return -1;
        }

        for (int j = row.length-1; j >= 0; j--) {
            StringBuilder sql = new StringBuilder("Delete From ");
            sql.append(model.getDatabaseName()).append(" where ");
            sql.append(pk[0]).append(" = \"").append(row[j][pkColumnNum[0]]).append("\"");
            for (int i = 1; i < pk.length; i++) {
                sql.append(" and ").append(pk[i]).append(" = \"").append(row[j][pkColumnNum[i]]).append("\"");
            }
            sqls.add(sql.toString());
        }

        int sum = 0;
        for (String x : sqls) {
            try {
                System.out.println(x);
                Statement stmt = con.createStatement();
                sum += stmt.executeUpdate(x);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return sum;
    }
    private int alterData(AllModel model, TableModelControl table) {
        List<String> sqls = new ArrayList<>();
        String[] col = ObjectArryToStringArray(table.getColumnNames());//将object转换为String
        if(col==null) return -1;
        Object[][] row = table.getRows();
        String[] pk = model.getPrimaryKey();
        int[] pkColumnNum = pKeyCheck(pk, col);//记录

        if (pkColumnNum == null) {
            return -1;
        }

        for (int i =0; i <row.length; i++) {
            StringBuilder sql = new StringBuilder("UPDATE ");
            sql.append(model.getDatabaseName()).append(" set ");
            for(int j=0;j < col.length;j++){
                sql.append(col[j]).append(" = ").append("\"").append(table.getTable().getValueAt(i,j)).append("\"  ,");
            }
            sql.delete(sql.length()-2,sql.length());
            sql.append(" where ");
            sql.append(pk[0]).append(" = \"").append(row[i][pkColumnNum[0]]).append("\"");
            for (int k = 1; k < pk.length; k++) {
                sql.append(" and ").append(pk[k]).append(" = \"").append(row[k][pkColumnNum[k]]).append("\"");
            }

            sqls.add(sql.toString());
        }

        int sum = 0;
        for (String x : sqls) {
            try {
                Statement stmt = con.createStatement();
                sum += stmt.executeUpdate(x);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println(x);
        }
        return sum;

    }

    public String[] ObjectArryToStringArray(Object[] objectArray) {
        if (objectArray == null) {
            return null;
        }

        String[] stringArray = new String[objectArray.length];
        for (int i = 0; i < objectArray.length; i++) {
            if (objectArray[i] != null) {
                stringArray[i] = objectArray[i].toString();
            } else {
                stringArray[i] = null;
            }
        }
        return stringArray;
    }

    public class JFrameModel {
        BatchGeneration batchGeneration=new BatchGeneration();

        JPanel getAlterPanel(AllModel model,TableModelControl table){
            JButton[] AlterButton=batchGeneration.createJButton(new String[]{"清空表格","上传修改"});
            JPanel AlterPanel=new JPanel(new FlowLayout());
            AlterPanel.add(AlterButton[0]);//清空表格
            AlterPanel.add(AlterButton[1]);//上传修改



            AlterButton[0].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    table.clear();
                }
            });


            AlterButton[1].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    alterData(model,table);
                }
            });
            return AlterPanel;
        }

        JPanel getAddPanel(AllModel model,TableModelControl table){
            JButton[] AlterButton=batchGeneration.createJButton(new String[]{"清空表格","新增行","增加记录"});
            JPanel AlterPanel=new JPanel(new FlowLayout());
            AlterPanel.add(AlterButton[0]);//清空表格
            AlterPanel.add(AlterButton[1]);//上传修改
            AlterPanel.add(AlterButton[2]);//上传修改




            AlterButton[0].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    table.clear();
                }
            });


            AlterButton[1].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    alterData(model,table);
                }
            });
            return AlterPanel;
        }


    }




}


