package cn.popularSciencesManager.JFrame;

import cn.popularSciencesManager.model.*;
import cn.popularSciencesManager.utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserFrame extends JFrame {
    static String databaseName = "sciences";
    //2.获取数据库连接
    static String URL = "127.0.0.1:3306";
    static String url = "jdbc:mysql://" + URL + "/" + databaseName + "?serverTimezone=GMT%2B8  &  useSSL=true & characterEncoding=utf8";
    static String username = "root";
    static String password = "12345600O";
    static Connection con;
    static Map<String, String[]> databaseStruct;//数据表，用于存储表格与
    static private Boolean isLogin = false;

    static {
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    QueryBuilder queryBuilder = new QueryBuilder();
    JDialogPromptWindow jdpw = new JDialogPromptWindow();
    TableModelControl articlesSearch = new TableModelControl(TableModelControl.isCellEditable.UNCellEditable, con);

    TableModelControl myArticle = new TableModelControl(TableModelControl.isCellEditable.CellEditable, con);

    TableModelControl articleView = new TableModelControl(TableModelControl.isCellEditable.UNCellEditable, con);

    TableModelControl userAlter = new TableModelControl(TableModelControl.isCellEditable.CellEditable, con);

    TableModelControl reviewSearch = new TableModelControl(TableModelControl.isCellEditable.UNCellEditable, con);

    TableModelControl articleTypeSearch = new TableModelControl(TableModelControl.isCellEditable.UNCellEditable, con);

    TableModelControl favoriteSearch = new TableModelControl(TableModelControl.isCellEditable.UNCellEditable, con);

    ArticlesModel articlesModel = new ArticlesModel();

    FavoriteModel favoriteModel = new FavoriteModel();

    ReviewModel reviewModel = new ReviewModel();

    UserModel userModel = new UserModel();

    UserModel user = new UserModel();

    DbUtils dbUtils = new DbUtils(con, databaseName);
    BatchGeneration batchGeneration = new BatchGeneration();

    public UserFrame() {

    }
    /***
     * 创建选项卡式窗体
     * @return 返回已创造号的选项卡式窗体
     */


    public UserFrame(UserModel user, Connection con) throws SQLException {
        this.user = user;
        setSize(800, 600);
        this.setTitle(user.getUserType() + ":" + user.getUserName());
        this.setLayout(new BorderLayout());

        databaseStruct = dbUtils.getDatabaseStruct();
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
        JMenuItem[] menuItem = batchGeneration.createJMenuItems(new String[]{"查看文章", "用户管理", "我的评论", "我的收藏"});
        for (int i = 0; i < menuItem.length; i++) {
            menu.add(menuItem[i]);
        }

        this.setJMenuBar(menu);

        //第一个选项卡：


        //创建选项板

        //文章相关选项
        String[] sTemp;
        sTemp = new String[]{"文章集", "查看文章", "发布文章", "管理我的文章"};
        JPanel[] articlesPanels = batchGeneration.createJPanel(sTemp.length);
        for (JPanel x : articlesPanels) {
            x.setLayout(new BorderLayout());
        }//写入布局管理器

        JTabbedPane articlesTabbedPane = batchGeneration.createJTabbedPane(sTemp, articlesPanels);//创建选项版


        //文章查询
        articlesPanels[0].add(articlesSearch.getTablePanel(), BorderLayout.CENTER);//获取组件


        JButton[] articlesSearchButton = batchGeneration.createJButton(new String[]{"查询文章信息", "查询文章信息", "查看文章", "清空表格","收藏文章"});
        JPanel[] articlesSearchPanels = batchGeneration.createJPanel(5);
        articlesSearchPanels[0].setLayout(new GridLayout(2, 2));
        for (int i = 1; i < 5; i++) {
            articlesSearchPanels[i].setLayout(new FlowLayout(FlowLayout.LEFT));
            articlesSearchPanels[0].add(articlesSearchPanels[i]);
        }
        articlesSearchPanels[1].add(articlesSearchButton[0]);//查询文章信息
        articlesSearchPanels[1].add(articlesSearchButton[1]);//查询文章信息
        articlesSearchPanels[2].add(articlesSearchButton[2]);//查看文章
        articlesSearchPanels[2].add(articlesSearchButton[3]);//清空表格
        articlesSearchPanels[3].add(articlesSearchButton[4]);//收藏文章

        articlesPanels[0].add(articlesSearchPanels[0], BorderLayout.SOUTH);
        articlesSearchButton[0].addActionListener(new ActionListener() {//查询文章信息
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

        articlesSearchButton[1].addActionListener(new ActionListener() {//筛选文章信息
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                articlesSearch.clear();
                SelectDialog select = new SelectDialog(articlesModel, articleTypeSearch, databaseStruct, queryBuilder
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
                String sqls = select.init();
                System.out.println("sql=" + sqls);
                articlesSearch.loadTableData(sqls);

            }
        });

        articlesSearchButton[3].addActionListener(new ActionListener() {//清空表格
            @Override
            public void actionPerformed(ActionEvent e) {
                articlesSearch.clear();
            }
        });

        articlesSearchButton[4].addActionListener(new ActionListener() {//收藏文章
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> sqls = new ArrayList<>();
                String[] col = ObjectArryToStringArray(articlesSearch.getColumnNames());
                Object[][] row = articlesSearch.getRows();
                String[] pk = {"articleID"};
                int[] pkColumnNum = pKeyCheck(pk, col);//记录

                if (pkColumnNum == null) {
                    return;
                }
                int[] selectedRows = articlesSearch.getTable().getSelectedRows();


                for (int j = selectedRows.length - 1; j >= 0; j--) {
                    StringBuilder sql = new StringBuilder("INSERT INTO favorite");
                    sql.append(" VALUES ( \"");
                    sql.append(user.getUserID()).append("\" , \"");
                    sql.append(row[selectedRows[j]][pkColumnNum[0]]).append("\" )");
                    sqls.add(sql.toString());
                }

                if(sqls==null)
                    return;

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


                if (sum == 0) {
                    jdpw.createInfoDialog(UserFrame.this,"收藏失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                else{
                    jdpw.createInfoDialog(UserFrame.this,"成功收藏"+sum+"篇文章", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);

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

        //查看文章
        articlesSearchButton[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sql;
                String[] col = ObjectArryToStringArray(articlesSearch.getColumnNames());
                Object[][] row = articlesSearch.getRows();
                String[] pk = articlesModel.getPrimaryKey();
                int[] pkColumnNum = pKeyCheck(pk, col);//记录

                if (pkColumnNum == null) {
                    return;
                }
                int[] selectedRows = articlesSearch.getTable().getSelectedRows();
                sql = new StringBuilder("Select user.userName , user.userType ,articles.articleID,articles.title, articles.content," +
                        "articles.createTime,articles.type FROM user,articles");
                sql.append(" where user.userID = articles.authorID and (");
                for (int j = selectedRows.length - 1; j >= 0; j--) {
                    sql.append("articles.").append(pk[0]).append(" = \"").append(row[selectedRows[j]][pkColumnNum[0]]).append("\"");
                    for (int i = 1; i < pk.length; i++) {
                        sql.append(" and ").append("articles.").append(pk[i]).append(" = \"").append(row[selectedRows[j]][pkColumnNum[i]]).append("\"");
                    }
                    sql.append(" or ");

                }
                sql.delete(sql.length() - 4, sql.length() - 1);
                sql.append(")");
                System.out.println(sql);
                articleView.loadTableData(sql.toString());
                articleView.setPageSize(1);


                articlesPanels[1].removeAll();
                articlesPanels[1].repaint();
                //顶部区域
                JPanel articleViewTopPanel = new JPanel(new FlowLayout());
                JLabel topTitle = new JLabel(articleView.getTable().getValueAt(0, 3).toString());
                articleViewTopPanel.add(topTitle);


                //中心区域
                JPanel articleViewCenterBox = new JPanel(new BorderLayout());//设置垂直分布的BOX；
                //第一层：作者信息显示
                JPanel[] articleViewInfo = batchGeneration.createJPanel(3);
                articleViewInfo[0].setLayout(new GridLayout(1, 2));
                articleViewInfo[1].setLayout(new FlowLayout(FlowLayout.LEFT));
                articleViewInfo[2].setLayout(new FlowLayout(FlowLayout.RIGHT));
                articleViewInfo[0].add(articleViewInfo[1]);
                articleViewInfo[0].add(articleViewInfo[2]);
                JLabel[] articleViewGetInfoLabel = batchGeneration.createJLabel(new String[]{"作者:" + articleView.getTable().getValueAt(0, 0) + " " + articleView.getTable().getValueAt(0, 1),
                        "发布板块:" + articleView.getTable().getValueAt(0, 6) + "创作时间: " + articleView.getTable().getValueAt(0, 5)});//顶部信息栏

                articleViewInfo[1].add(articleViewGetInfoLabel[0]);//作者信息
                articleViewInfo[2].add(articleViewGetInfoLabel[1]);//创造信息
                articleViewCenterBox.add(articleViewInfo[0], BorderLayout.NORTH);
                //中心第二层：内容获取
                JTextArea articleViewArea = new JTextArea();//创建输入文本框


                Font defaultTextAreaFont=UIManager.getFont("TextArea.font");
                Font textAreaFont=defaultTextAreaFont.deriveFont(Font.PLAIN,defaultTextAreaFont.getSize()+3.0f);
                articleViewArea.setFont(textAreaFont);

                articleViewArea.setLineWrap(true);//字段换行
                articleViewArea.setWrapStyleWord(true);//单词换行
                articleViewArea.setEnabled(false);
                articleViewArea.setDisabledTextColor(Color.BLACK);
                JScrollPane articleViewScroll = new JScrollPane(articleViewArea);
                articleViewArea.setText(articleView.getTable().getValueAt(0, 4).toString());
                articleViewCenterBox.add(articleViewScroll, BorderLayout.CENTER);


                //窗体对表格的控制
                JPanel articleViewControl = new JPanel(new FlowLayout());
                JButton[] articleViewControlButtons = batchGeneration.createJButton(new String[]{"上一篇", "跳转到页", "下一篇"});
                JTextField pageSetField = new JTextField(3);
                JLabel pageInfo = articleView.getTableInfo()[0];//将表格的此窗体获取;
                articleViewControl.add(articleViewControlButtons[0]);
                articleViewControl.add(pageSetField);
                articleViewControl.add(pageInfo);
                articleViewControl.add(articleViewControlButtons[1]);
                articleViewControl.add(articleViewControlButtons[2]);
                articleViewCenterBox.add(articleViewControl, BorderLayout.SOUTH);
                //第三层：额外功能

                //底部窗口
                JPanel[] articleViewBottomPanel = batchGeneration.createJPanel(3);
                articleViewBottomPanel[0].setLayout(new GridLayout(2, 1));
                articleViewBottomPanel[1].setLayout(new FlowLayout());
                articleViewBottomPanel[2].setLayout(new FlowLayout());
                articleViewBottomPanel[0].add(articleViewBottomPanel[1]);
                articleViewBottomPanel[0].add(articleViewBottomPanel[2]);//设置基本布局
                JButton[] articleViewBottomButton = batchGeneration.createJButton(new String[]{"下载", "清空"});
                articleViewBottomPanel[1].add(articleViewBottomButton[0]);
                articleViewBottomPanel[1].add(articleViewBottomButton[1]);//设置按钮
                JLabel infoLable = new JLabel();//提示窗
                articleViewBottomPanel[2].add(infoLable);
                articleViewBottomPanel[0].add(articleViewBottomPanel[2]);

                //顶部主窗体添加
                articlesPanels[1].add(articleViewTopPanel, BorderLayout.NORTH);
                //中心区域主窗体添加
                articlesPanels[1].add(new JPanel(), BorderLayout.EAST);
                articlesPanels[1].add(new JPanel(), BorderLayout.WEST);
                articlesPanels[1].add(articleViewCenterBox, BorderLayout.CENTER);
                //底部主体窗口添加
                articlesPanels[1].add(articleViewBottomPanel[0], BorderLayout.SOUTH);

                //上一篇
                articleViewControlButtons[0].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (articleView.getAllRows() == null) {
                            infoLable.setText("表格为空");
                            return;
                        }
                        articleView.setCurrentPage(articleView.getCurrentPage() - 1);
                        articleViewGetInfoLabel[0].setText("作者:" + articleView.getTable().getValueAt(0, 0) + " " + articleView.getTable().getValueAt(0, 1));
                        articleViewGetInfoLabel[1].setText("发布板块:" + articleView.getTable().getValueAt(0, 6) + "  创作时间: " + articleView.getTable().getValueAt(0, 5));
                        topTitle.setText(articleView.getTable().getValueAt(0, 3).toString());
                        articleViewArea.setText(articleView.getTable().getValueAt(0, 4).toString());
                    }
                });

                //跳转
                articleViewControlButtons[1].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (articleView.getAllRows() == null) {
                                infoLable.setText("表格为空");
                                pageSetField.setText(null);
                                return;
                            }
                            String text = pageSetField.getText();
                            if (text == null) {
                                infoLable.setText("请输入整数");
                                pageSetField.setText(null);
                                return;
                            }
                            int current = Integer.parseInt(text);
                            if (current < 0) {
                                infoLable.setText("请输入大于0的整数");
                                pageSetField.setText(null);
                                return;
                            }
                            articleView.setCurrentPage(current);
                            pageSetField.setText(null);
                            infoLable.setText(null);
                        } catch (NumberFormatException ex) {
                            infoLable.setText("请输入整数");
                            pageSetField.setText(null);
                        }
                    }
                });

                //下一篇
                articleViewControlButtons[2].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (articleView.getAllRows() == null) {
                            infoLable.setText("表格为空");
                            return;
                        }
                        articleView.setCurrentPage(articleView.getCurrentPage() + 1);
                        articleViewGetInfoLabel[0].setText("作者:" + articleView.getTable().getValueAt(0, 0) + " " + articleView.getTable().getValueAt(0, 1));
                        articleViewGetInfoLabel[1].setText("发布板块:" + articleView.getTable().getValueAt(0, 6) + "   创作时间: " + articleView.getTable().getValueAt(0, 5));
                        topTitle.setText(null);
                        topTitle.setText(articleView.getTable().getValueAt(0, 3).toString());
                        articleViewArea.setText(articleView.getTable().getValueAt(0, 4).toString());
                        articlesPanels[1].repaint();
                    }
                });


                //下载
                articleViewBottomButton[0].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser jf = new JFileChooser();
                        jf.showSaveDialog(null);
                        jf.setVisible(true);
                        File file = jf.getSelectedFile();
                        String text ="作者:" + articleView.getTable().getValueAt(0, 0) + " " + articleView.getTable().getValueAt(0, 1)+'\n'+
                        "发布板块:" + articleView.getTable().getValueAt(0, 6) + "   创作时间: " + articleView.getTable().getValueAt(0, 5)+'\n'+articleViewArea.getText();
                        // 写入文件
                        try {
                            Files.write(file.toPath(), text.getBytes());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                //清空
                articleViewBottomButton[1].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        articleView.clear();
                        articleViewGetInfoLabel[0].setText(null);
                        articleViewGetInfoLabel[1].setText(null);
                        topTitle.setText(null);
                        articleViewArea.setText(null);
                        articlesPanels[1].repaint();
                    }
                });

            }
        });

        //顶部区域
        JPanel articleAddTopPanel = new JPanel(new FlowLayout());
        articleAddTopPanel.add(new JLabel("发布文章"));


        //中心区域
        JPanel articleAddCenterBox = new JPanel(new BorderLayout());//设置垂直分布的BOX；
        //第一层：板块选择
        JPanel[] articleAddGetInfo = batchGeneration.createJPanel(3);
        articleAddGetInfo[0].setLayout(new GridLayout(1, 2));
        articleAddGetInfo[1].setLayout(new FlowLayout(FlowLayout.LEFT));
        articleAddGetInfo[2].setLayout(new FlowLayout(FlowLayout.RIGHT));
        articleAddGetInfo[0].add(articleAddGetInfo[1]);
        articleAddGetInfo[0].add(articleAddGetInfo[2]);
        JLabel[] articleAddGetInfoLabel = batchGeneration.createJLabel(new String[]{"文章标题:", "发布板块"});
        JTextField articleAddGetTitle = new JTextField(30);
        List<String> articleTypeName = new ArrayList<>();
        try {
            String s = "SELECT type from articletype";
            PreparedStatement pr = con.prepareStatement(s);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                articleTypeName.add(rs.getString("type"));
            }
        } catch (SQLException ea) {
            ea.printStackTrace();
        }
        JComboBox<String> articleAddGetTypeComboBox = new JComboBox<>(ObjectArryToStringArray(articleTypeName.toArray()));//创建板块选择框
        articleAddGetInfo[1].add(articleAddGetInfoLabel[0]);
        articleAddGetInfo[1].add(articleAddGetTitle);
        articleAddGetInfo[2].add(articleAddGetInfoLabel[1]);
        articleAddGetInfo[2].add(articleAddGetTypeComboBox);
        articleAddCenterBox.add(articleAddGetInfo[0], BorderLayout.NORTH);
        //中心第二层：内容获取
        JTextArea articleGetArea = new JTextArea();//创建输入文本框
        JScrollPane articleAddScroll = new JScrollPane(articleGetArea);
        articleAddCenterBox.add(articleAddScroll, BorderLayout.CENTER);
        //第三层：额外功能
        JButton articleGet = new JButton("载入现有文章");
        JPanel articleGetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        articleGetPanel.add(articleGet);
        articleAddCenterBox.add(articleGetPanel, BorderLayout.SOUTH);

        //底部窗口
        JPanel[] articleBottomPanel = batchGeneration.createJPanel(3);
        articleBottomPanel[0].setLayout(new GridLayout(1, 2));
        articleBottomPanel[0].setLayout(new FlowLayout());
        articleBottomPanel[0].setLayout(new FlowLayout());
        articleBottomPanel[0].add(articleBottomPanel[1]);
        articleBottomPanel[0].add(articleBottomPanel[2]);//设置基本布局
        JButton[] articleBottomButton = batchGeneration.createJButton(new String[]{"发布", "清空"});
        articleBottomPanel[1].add(articleBottomButton[0]);
        articleBottomPanel[2].add(articleBottomButton[1]);//设置按钮


        //顶部主窗体添加
        articlesPanels[2].add(articleAddTopPanel, BorderLayout.NORTH);
        //中心区域主窗体添加
        articlesPanels[2].add(new JPanel(), BorderLayout.EAST);
        articlesPanels[2].add(new JPanel(), BorderLayout.WEST);
        articlesPanels[2].add(articleAddCenterBox, BorderLayout.CENTER);
        //底部主体窗口添加
        articlesPanels[2].add(articleBottomPanel[0], BorderLayout.SOUTH);


        //载入现有文章
        articleGet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jf = new JFileChooser();
                jf.showOpenDialog(null);
                jf.setVisible(true);
                File file = jf.getSelectedFile();//获取文件
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
                if (articleAddGetTitle.getText().isEmpty()) {
                    jdpw.createInfoDialog(UserFrame.this, "请输入标题", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                try {
                    String sql = "INSERT INTO articles VALUES( ? , ? , ? , ? , ? , ?)";
                    PreparedStatement pr = con.prepareStatement(sql);
                    pr.setNull(1, Types.INTEGER);
                    pr.setInt(2, user.getUserID());
                    pr.setString(3, articleAddGetTitle.getText());
                    pr.setString(4, articleGetArea.getText());
                    pr.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    pr.setString(6, articleAddGetTypeComboBox.getSelectedItem().toString());
                    if (pr.executeUpdate() == 0) {
                        jdpw.createInfoDialog(UserFrame.this, "发布文章失败，请重试", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                    } else {
                        jdpw.createInfoDialog(UserFrame.this, "文章发表成功", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                        articleAddGetTitle.setText(null);
                        articleGetArea.setText(null);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        //清空
        articleBottomButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        //文章查看


        articlesPanels[3].add(myArticle.getTablePanel(), BorderLayout.CENTER);//获取组件
        JButton[] myArticleButtom = batchGeneration.createJButton(new String[]{"查询文章信息", "查找信息", "删除全部记录", "删除被选记录", "清空表格"});
        JPanel[] myArticlePanels = batchGeneration.createJPanel(5);
        myArticlePanels[0].setLayout(new GridLayout(2, 2));
        for (int i = 1; i < 5; i++) {
            myArticlePanels[i].setLayout(new FlowLayout(FlowLayout.LEFT));
            myArticlePanels[0].add(myArticlePanels[i]);
        }
        myArticlePanels[1].add(myArticleButtom[0]);//查询全部信息
        myArticlePanels[1].add(myArticleButtom[1]);//查找信息
        myArticlePanels[2].add(myArticleButtom[2]);//删除全部记录
        myArticlePanels[2].add(myArticleButtom[3]);//删除被选记录
        myArticlePanels[3].add(myArticleButtom[4]);//清空表格
        articlesPanels[3].add(myArticlePanels[0], BorderLayout.SOUTH);
        myArticleButtom[0].addActionListener(new ActionListener() {//查询全部信息
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                myArticle.clear();
                myArticle.loadTableData(queryBuilder
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
                        .where("user.userID = \"" + user.getUserID() + "\"")
                        .build());

            }
        });

        myArticleButtom[1].addActionListener(new ActionListener() {//查找信息
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                myArticle.clear();
                SelectDialog select = new SelectDialog(articlesModel, articleTypeSearch, databaseStruct, queryBuilder
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
                        .where("user.userID = \"" + user.getUserID() + "\"")
                        .build());                String sqls = select.init();
                System.out.println("sql=" + sqls);

                myArticle.loadTableData(sqls);

            }
        });

        myArticleButtom[2].addActionListener(new ActionListener() {//删除全部记录
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = deleteAllRows(articlesModel, myArticle);
                if (result == -1) {
                    jdpw.createInfoDialog(UserFrame.this, "删除失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                } else {
                    jdpw.createInfoDialog(UserFrame.this, "成功删除了" + result + "条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);

                }
                queryBuilder = new QueryBuilder();
                myArticle.clear();
                myArticle.loadTableData(queryBuilder
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
                        .where("user.userID = \"" + user.getUserID() + "\"")
                        .build());

            }
        });

        myArticleButtom[3].addActionListener(new ActionListener() {//删除被选记录
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> sql = deleteSelectRows(articlesModel, myArticle);
                if (sql == null)
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
                    jdpw.createInfoDialog(UserFrame.this, "删除操作执行失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                } else {
                    jdpw.createInfoDialog(UserFrame.this, "成功删除了" + sum + "条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);

                }

                queryBuilder = new QueryBuilder();
                myArticle.clear();
                myArticle.loadTableData(queryBuilder
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
                        .where("user.userID = \"" + user.getUserID() + "\"")
                        .build());
            }
        });


        myArticleButtom[4].addActionListener(new ActionListener() {//清空表格
            @Override
            public void actionPerformed(ActionEvent e) {
                articlesSearch.clear();
            }
        });

        //用户管理
        sTemp = new String[]{"信息维护"};
        JPanel[] userInfoPanels = batchGeneration.createJPanel(sTemp.length);
        for (JPanel x : userInfoPanels) {
            x.setLayout(new BorderLayout());
        }
        JTabbedPane userInfo = batchGeneration.createJTabbedPane(sTemp, userInfoPanels);

        //创建垂直盒子
        Box userBox = Box.createVerticalBox();
        userInfoPanels[0].add(userBox);
        userBox.setBorder(new EmptyBorder(0, 50, 0, 50));
        JPanel[] userInfoPrint = batchGeneration.createJPanel(5);
        for (JPanel panel : userInfoPrint) {
            panel.setLayout(new FlowLayout());
            panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        }
        JLabel[] userInfoLabels = batchGeneration.createJLabel(new String[]{"用户名 ", "email ", "用户ID ", "创建日期", "用户类型"});
        JTextField[] userInfoField = batchGeneration.createJTextField(5, 10);

        for (int i = 0; i < userInfoField.length; i++) {//将信息框封装
            userInfoPrint[i].add(userInfoLabels[i]);
            userInfoPrint[i].add(userInfoField[i]);
            userInfoField[i].setEnabled(false);
            userBox.add(userInfoPrint[i]);

        }
        //存入信息；
        userInfoField[0].setText(user.getUserName());
        userInfoField[1].setText(user.getEmail());
        userInfoField[2].setText(String.valueOf(user.getUserID()));
        userInfoField[3].setText(user.getCreateData());
        userInfoField[4].setText(user.getUserType());

        JPanel userButtomButtonPanel = new JPanel(new FlowLayout());
        JButton[] userButtomButton = batchGeneration.createJButton(new String[]{"修改密码", "修改信息"});

        final boolean[] userFlag = {false};

        userButtomButtonPanel.add(userButtomButton[0]);
        userButtomButtonPanel.add(userButtomButton[1]);
        userBox.add(userButtomButtonPanel);

        //修改密码
        userButtomButton[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int userID = user.getUserID();
                final boolean[] flag = {false};
                JDialog resetPass = new JDialog(UserFrame.this);
                resetPass.setTitle("重设密码");
                resetPass.setLayout(new FlowLayout());
                JPanel addTypePanel = new JPanel(new BorderLayout());
                resetPass.add(addTypePanel);


                JPanel[] topPanel = batchGeneration.createJPanel(3);
                topPanel[0].setLayout(new GridLayout(1, 1));
                topPanel[1].setLayout(new FlowLayout());
                JLabel topLabel = new JLabel("请输入新密码");
                topPanel[1].add(topLabel);
                topPanel[0].add(topPanel[1]);
                topPanel[2].setLayout(new FlowLayout(FlowLayout.RIGHT));
                topPanel[0].add(topPanel[2]);
                addTypePanel.add(topPanel[0], BorderLayout.NORTH);//添加顶部到主窗口


                //设置中心主显示页面
                //设置垂直盒子
                JPanel centerPanel = new JPanel(new FlowLayout());
                Box centerBox = Box.createVerticalBox();
                centerPanel.add(centerBox);
                addTypePanel.add(centerPanel, BorderLayout.CENTER);

                JPanel[] infoTypeIn = batchGeneration.createJPanel(2);
                for (JPanel panel : infoTypeIn) {
                    panel.setLayout(new GridLayout(1, 2));
                }

                JLabel[] infoLabel = batchGeneration.createJLabel(new String[]{"请输入密码", "请再次输入密码"});
                JPasswordField passwordField = new JPasswordField(10);
                JPasswordField passwordField1 = new JPasswordField(10);
                JPanel[] passwordPanel = batchGeneration.createJPanel(2);
                for (JPanel panel : passwordPanel) {
                    panel.setLayout(new GridLayout(1, 2));
                }

                passwordPanel[0].add(infoLabel[0]);
                passwordPanel[0].add(passwordField);
                centerBox.add(passwordPanel[0]);
                passwordPanel[1].add(infoLabel[1]);
                passwordPanel[1].add(passwordField1);
                centerBox.add(passwordPanel[1]);

                //底部窗口创建
                JPanel bottomArea = new JPanel(new FlowLayout());
                JButton[] southButton = batchGeneration.createJButton(new String[]{"确认修改", "取消"});
                bottomArea.add(southButton[0]);
                bottomArea.add(southButton[1]);
                addTypePanel.add(bottomArea, BorderLayout.SOUTH);

                //创建账号按钮
                southButton[0].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 检查密码是否一致
                        if (!Arrays.equals(passwordField.getPassword(), passwordField1.getPassword())) {
                            jdpw.createInfoDialog(resetPass, "两次输入的密码不一致", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                            return;
                        }
                        try {
                            String sql = "UPDATE user SET password = ? WHERE userID = ?";
                            PreparedStatement pre = con.prepareStatement(sql);
                            pre.setString(1, new String(passwordField.getPassword()));
                            pre.setInt(2, userID);
                            if (pre.executeUpdate() == 0) {
                                jdpw.createInfoDialog(resetPass, "密码修改失败，请联系管理员解决问题", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                            } else {
                                jdpw.createInfoDialog(resetPass, "密码修改成功!!", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                                resetPass.dispose();
                            }
                            pre.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                resetPass.setLocationRelativeTo(null);//设置居中
                resetPass.pack();
                resetPass.setModal(true);
                resetPass.setVisible(true);
            }
        });

        //修改信息
        userButtomButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userFlag[0]) {
                    if (userInfoField[0].getText().isEmpty()) {
                        jdpw.createInfoDialog(UserFrame.this, "请输入新的用户名", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                        return;
                    }
                    try {
                        String sql = "UPDATE user set userName = ? , email = ? where userID = ?";
                        PreparedStatement pre = con.prepareStatement(sql);
                        pre.setString(1, userInfoField[0].getText());
                        if (userInfoField[0].getText().isEmpty()) {
                            pre.setNull(2, Types.VARCHAR);
                        } else pre.setString(2, userInfoField[1].getText());
                        pre.setInt(3, Integer.parseInt(userInfoField[2].getText()));
                        if (pre.executeUpdate() == 0) {
                            jdpw.createInfoDialog(UserFrame.this, "用户名重复，请重新输入", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                        } else {
                            jdpw.createInfoDialog(UserFrame.this, "信息修改成功！", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                            for (int i = 0; i < 2; i++) {//将信息框封装
                                userInfoField[i].setEnabled(false);
                                user.setUserName(userInfoField[0].getText());
                                user.setEmail(userInfoField[0].getText());
                                setTitle("用户：" + user.getUserName());
                                userFlag[0] = false;
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }


                } else {
                    for (int i = 0; i < 2; i++) {//将信息框封装
                        userInfoField[i].setEnabled(true);
                    }
                    userButtomButton[1].setText("上传修改");
                    UserFrame.this.repaint();
                    userFlag[0] = true;
                }


            }
        });


        //评论相关选项
        sTemp = new String[]{"查找评论"};
        JPanel[] reviewPanels = batchGeneration.createJPanel(sTemp.length);
        for (JPanel x : reviewPanels) {
            x.setLayout(new BorderLayout());
        }
        JTabbedPane reviewTabbedPane = batchGeneration.createJTabbedPane(sTemp, reviewPanels);


        //查找评论
        reviewPanels[0].add(reviewSearch.getTablePanel(), BorderLayout.CENTER);

        JButton[] reviewSearchButton = batchGeneration.createJButton(new String[]{"查询评论信息", "筛选评论信息", "删除全部评论", "删除被选评论", "清空表格"});
        JPanel[] reviewSearchPanels = batchGeneration.createJPanel(5);
        reviewSearchPanels[0].setLayout(new GridLayout(2, 2));
        for (int i = 1; i < 5; i++) {
            reviewSearchPanels[i].setLayout(new FlowLayout(FlowLayout.LEFT));
            reviewSearchPanels[0].add(reviewSearchPanels[i]);
        }
        reviewSearchPanels[1].add(reviewSearchButton[0]);//查询评论信息
        reviewSearchPanels[1].add(reviewSearchButton[1]);//筛选评论信息
        reviewSearchPanels[2].add(reviewSearchButton[2]);//删除全部评论
        reviewSearchPanels[2].add(reviewSearchButton[3]);//删除被选评论
        reviewSearchPanels[3].add(reviewSearchButton[4]);//清空表格
        reviewPanels[0].add(reviewSearchPanels[0], BorderLayout.SOUTH);

        reviewSearchButton[0].addActionListener(new ActionListener() {//查询评论信息
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                reviewSearch.clear();
                reviewSearch.loadTableData(queryBuilder
                        .select("review.articleID")
                        .select("articles.title")
                        .select("review.review")
                        .select("review.createTime")
                        .select("review.likeNum")
                        .select("articles.type")
                        .from("articles")
                        .from("user")
                        .from("review")
                        .where("review.userID = user.userID")
                        .where("review.articleID = articles.articleID")
                        .where("user.userID = \"" + user.getUserID() + "\"")
                        .build());
            }
        });


        //筛选评论信息
        reviewSearchButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                reviewSearch.clear();
                SelectDialog select = new SelectDialog(reviewModel, reviewSearch, databaseStruct, queryBuilder
                        .select("review.articleID")
                        .select("articles.title")
                        .select("review.review")
                        .select("review.createTime")
                        .select("review.likeNum")
                        .select("articles.type")
                        .from("articles")
                        .from("user")
                        .from("review")
                        .where("review.userID = user.userID")
                        .where("review.articleID = articles.articleID")
                        .where("user.userID = \"" + user.getUserID() + "\"")
                        .build());
                reviewSearch.loadTableData(select.init());
            }
        });


        //删除全部评论
        reviewSearchButton[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = deleteAllRows(reviewModel, reviewSearch);
                if (result == -1) {
                    jdpw.createInfoDialog(UserFrame.this, "删除失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                } else {
                    jdpw.createInfoDialog(UserFrame.this, "成功删除了" + result + "条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                queryBuilder = new QueryBuilder();
                reviewSearch.clear();
                reviewSearch.loadTableData(queryBuilder
                        .select("review.articleID")
                        .select("articles.title")
                        .select("review.review")
                        .select("review.createTime")
                        .select("review.likeNum")
                        .select("articles.type")
                        .from("articles")
                        .from("user")
                        .from("review")
                        .where("review.userID = user.userID")
                        .where("review.articleID = articles.articleID")
                        .where("user.userID = \"" + user.getUserID() + "\"")
                        .build());
            }
        });


        //删除被选评论
        reviewSearchButton[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> sql = deleteSelectRows(reviewModel, reviewSearch);
                if (sql == null)
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
                    jdpw.createInfoDialog(UserFrame.this, "删除操作执行失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                } else {
                    jdpw.createInfoDialog(UserFrame.this, "已成功删除" + sum + "条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                queryBuilder = new QueryBuilder();
                reviewSearch.clear();
                reviewSearch.loadTableData(queryBuilder
                        .select("review.articleID")
                        .select("articles.title")
                        .select("review.review")
                        .select("review.createTime")
                        .select("review.likeNum")
                        .select("articles.type")
                        .from("articles")
                        .from("user")
                        .from("review")
                        .where("review.userID = user.userID")
                        .where("review.articleID = articles.articleID")
                        .where("user.userID = \"" + user.getUserID() + "\"")
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


        //收藏夹管理
        sTemp = new String[]{"收藏数据表"};
        JPanel[] favoritePanels = batchGeneration.createJPanel(sTemp.length);
        for (JPanel x : favoritePanels) {
            x.setLayout(new BorderLayout());
        }
        JTabbedPane favoriteTabbedPane = batchGeneration.createJTabbedPane(sTemp, favoritePanels);

        //收藏数据表
        favoritePanels[0].add(favoriteSearch.getTablePanel(), BorderLayout.CENTER);

        JButton[] favoriteSearchButton = batchGeneration.createJButton(new String[]{"查询收藏信息", "筛选收藏信息", "删除全部收藏", "删除被选收藏", "清空表格"});
        JPanel[] favoriteSearchPanels = batchGeneration.createJPanel(5);
        favoriteSearchPanels[0].setLayout(new GridLayout(2, 2));
        for (int i = 1; i < 5; i++) {
            favoriteSearchPanels[i].setLayout(new FlowLayout(FlowLayout.LEFT));
            favoriteSearchPanels[0].add(favoriteSearchPanels[i]);
        }
        favoriteSearchPanels[1].add(favoriteSearchButton[0]);//查询收藏信息
        favoriteSearchPanels[1].add(favoriteSearchButton[1]);//筛选收藏信息
        favoriteSearchPanels[2].add(favoriteSearchButton[2]);//删除全部收藏
        favoriteSearchPanels[2].add(favoriteSearchButton[3]);//删除被选收藏
        favoriteSearchPanels[3].add(favoriteSearchButton[4]);//清空表格
        favoritePanels[0].add(favoriteSearchPanels[0], BorderLayout.SOUTH);

        favoriteSearchButton[0].addActionListener(new ActionListener() {//查询收藏信息
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                favoriteSearch.clear();
                favoriteSearch.loadTableData(queryBuilder
                        .select("articles.*")
                        .from("articles")
                        .from("favorite")
                        .from("user")
                        .where("favorite.userID = user.userID")
                        .where("favorite.articleID = articles.articleID")
                        .where("user.userID = \"" + user.getUserID() + "\"")
                        .build()
                );
            }
        });

        //筛选收藏信息
        favoriteSearchButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder = new QueryBuilder();
                favoriteSearch.clear();
                SelectDialog select = new SelectDialog(favoriteModel, favoriteSearch, databaseStruct, queryBuilder
                        .select("articles.*")
                        .from("articles")
                        .from("favorite")
                        .from("user")
                        .where("favorite.userID = user.userID")
                        .where("favorite.articleID = articles.articleID")
                        .where("user.userID = \"" + user.getUserID() + "\"")
                        .build());
                favoriteSearch.loadTableData(select.init());
            }
        });

        //删除全部收藏
        favoriteSearchButton[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> sqls = new ArrayList<>();
                String[] col = ObjectArryToStringArray(favoriteSearch.getColumnNames());//将object转换为String
                if (col == null) return;
                Object[][] row = favoriteSearch.getRows();
                String[] pk = {"articleID"};
                int[] pkColumnNum = pKeyCheck(pk, col);//记录

                if (pkColumnNum == null) {
                    return ;
                }

                for (int j = row.length - 1; j >= 0; j--) {
                    StringBuilder sql = new StringBuilder("Delete From ");
                    sql.append(favoriteModel.getDatabaseName()).append(" where ");
                    sql.append(pk[0]).append(" = \"").append(row[j][pkColumnNum[0]]).append("\"")
                            .append(" and ").append("userID").append(" = \"").append(user.getUserID()).append("\"");
                    for (int i = 1; i < pk.length; i++) {
                        sql.append(" and ").append(pk[i]).append(" = \"").append(row[j][pkColumnNum[i]]).append("\"");
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
                }

                if (sum == -1) {
                    jdpw.createInfoDialog(UserFrame.this, "删除失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                } else {
                    jdpw.createInfoDialog(UserFrame.this, "成功删除了" + sum + "条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                }
                queryBuilder = new QueryBuilder();
                favoriteSearch.clear();
                favoriteSearch.loadTableData(queryBuilder
                        .select("articles.*")
                        .from("articles")
                        .from("favorite")
                        .from("user")
                        .where("favorite.userID = user.userID")
                        .where("favorite.articleID = articles.articleID")
                        .where("user.userID = \"" + user.getUserID() + "\"")
                        .build());
            }
        });

        //删除被选收藏
        favoriteSearchButton[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sql;
                String[] col = ObjectArryToStringArray(favoriteSearch.getColumnNames());
                Object[][] row = favoriteSearch.getRows();
                String[] pk = {"articleID"};
                int[] pkColumnNum = pKeyCheck(pk, col);//记录

                if (pkColumnNum == null) {
                    return;
                }
                int[] selectedRows = favoriteSearch.getTable().getSelectedRows();
                sql = new StringBuilder("Delete From  ");
                sql.append(favoriteModel.getDatabaseName()).append(" where ");
                for (int j = selectedRows.length - 1; j >= 0; j--) {
                    sql.append("(").append(pk[0]).append(" = \"").append(row[selectedRows[j]][pkColumnNum[0]]).append("\" and userID=\"")
                            .append(user.getUserID()).append("\"");
                    sql.append(") or ");

                }
                sql.delete(sql.length() - 4, sql.length() - 1);
                System.out.println(sql);
                if (sql == null)
                    return;
                int sum = 0;
                    try {
                        Statement stmt = con.createStatement();
                        sum += stmt.executeUpdate(String.valueOf(sql));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }



                if (sum == 0) {
                    jdpw.createInfoDialog(UserFrame.this, "删除操作执行失败", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);
                } else {
                    jdpw.createInfoDialog(UserFrame.this, "成功删除了" + sum + "条记录", JDialogPromptWindow.DialogSelect.NOT_CLOSED_WINDOWS);

                }

                queryBuilder = new QueryBuilder();
                favoriteSearch.clear();
                favoriteSearch.loadTableData(queryBuilder
                        .select("articles.*")
                        .from("articles")
                        .from("favorite")
                        .from("user")
                        .where("favorite.userID = user.userID")
                        .where("favorite.articleID = articles.articleID")
                        .where("user.userID = \"" + user.getUserID() + "\"")
                        .build());


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
                UserFrame.this.getContentPane().removeAll();
                UserFrame.this.add(articlesTabbedPane, BorderLayout.CENTER);
                UserFrame.this.validate();
                UserFrame.this.repaint();
            }
        });

        //用户管理
        menuItem[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserFrame.this.getContentPane().removeAll();
                UserFrame.this.add(userInfo, BorderLayout.CENTER);
                UserFrame.this.validate();
                UserFrame.this.repaint();
            }
        });

        //评论管理
        menuItem[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserFrame.this.getContentPane().removeAll();
                UserFrame.this.add(reviewTabbedPane, BorderLayout.CENTER);
                UserFrame.this.validate();
                UserFrame.this.repaint();
            }
        });


        //收藏管理
        menuItem[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserFrame.this.getContentPane().removeAll();
                UserFrame.this.add(favoriteTabbedPane, BorderLayout.CENTER);
                UserFrame.this.validate();
                UserFrame.this.repaint();
            }
        });


        setLocationRelativeTo(null);//设置居中
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) throws SQLException {

        Main main = new Main(con, databaseName);
        main.login();
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
        sql.delete(sql.length() - 4, sql.length() - 1);
        System.out.println(sql);
        return sql.toString();
    }

    private int deleteAllRows(AllModel model, TableModelControl table) {
        List<String> sqls = new ArrayList<>();
        String[] col = ObjectArryToStringArray(table.getColumnNames());//将object转换为String
        if (col == null) return -1;
        Object[][] row = table.getRows();
        String[] pk = model.getPrimaryKey();
        int[] pkColumnNum = pKeyCheck(pk, col);//记录

        if (pkColumnNum == null) {
            return -1;
        }

        for (int j = row.length - 1; j >= 0; j--) {
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
        if (col == null) return -1;
        Object[][] row = table.getRows();
        String[] pk = model.getPrimaryKey();
        int[] pkColumnNum = pKeyCheck(pk, col);//记录

        if (pkColumnNum == null) {
            return -1;
        }

        for (int i = 0; i < row.length; i++) {
            StringBuilder sql = new StringBuilder("UPDATE ");
            sql.append(model.getDatabaseName()).append(" set ");
            for (int j = 0; j < col.length; j++) {
                sql.append(col[j]).append(" = ").append("\"").append(table.getTable().getValueAt(i, j)).append("\"  ,");
            }
            sql.delete(sql.length() - 2, sql.length());
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
        BatchGeneration batchGeneration = new BatchGeneration();

        JPanel getAlterPanel(AllModel model, TableModelControl table) {
            JButton[] AlterButton = batchGeneration.createJButton(new String[]{"清空表格", "上传修改"});
            JPanel AlterPanel = new JPanel(new FlowLayout());
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
                    alterData(model, table);
                }
            });
            return AlterPanel;
        }

        JPanel getAddPanel(AllModel model, TableModelControl table) {
            JButton[] AlterButton = batchGeneration.createJButton(new String[]{"清空表格", "新增行", "增加记录"});
            JPanel AlterPanel = new JPanel(new FlowLayout());
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
                    alterData(model, table);
                }
            });
            return AlterPanel;
        }


    }

}
