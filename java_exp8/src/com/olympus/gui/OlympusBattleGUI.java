package com.olympus.gui;

import com.olympus.entity.BattleRecord;
import com.olympus.entity.Character;
import com.olympus.system.BattleSystem;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 奥林匹斯诸神征讨泰坦 - GUI主窗口
 * 纯文字MUD风格，使用Swing基础组件实现
 */
public class OlympusBattleGUI extends JFrame {

    private BattleSystem battleSystem;
    private List<Character> gods;
    private List<Character> titans;
    private Character selectedGod;
    private Character selectedTitan;
    private int roundCount;
    private boolean battleEnded;

    // GUI组件
    private JTextArea battleLogArea;
    private JTextArea infoArea;
    private JComboBox<String> godCombo;
    private JComboBox<String> titanCombo;
    private JButton startBtn, attackBtn, skillBtn, defendBtn, fleeBtn, recordBtn, clearBtn;
    private JLabel godHpLabel, titanHpLabel, roundLabel, statusLabel;

    // 配色方案
    private static final Color BG_COLOR = new Color(30, 30, 45);
    private static final Color PANEL_BG = new Color(45, 45, 65);
    private static final Color TEXT_COLOR = new Color(220, 215, 200);
    private static final Color ACCENT_COLOR = new Color(255, 200, 80);
    private static final Color HP_COLOR = new Color(100, 200, 100);

    public OlympusBattleGUI() {
        battleSystem = new BattleSystem();
        gods = battleSystem.initGods();
        titans = battleSystem.initTitans();
        roundCount = 0;
        battleEnded = false;

        initFrame();
        initComponents();
        layoutComponents();
        addEventListeners();

        setVisible(true);
    }

    private void initFrame() {
        setTitle("奥林匹斯诸神征讨泰坦 - 文字MUD战斗系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 680);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);
        setLayout(new BorderLayout(8, 8));
    }

    private void initComponents() {
        // 战斗日志区
        battleLogArea = new JTextArea();
        battleLogArea.setEditable(false);
        battleLogArea.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        battleLogArea.setBackground(new Color(25, 25, 35));
        battleLogArea.setForeground(TEXT_COLOR);
        battleLogArea.setLineWrap(true);
        battleLogArea.setWrapStyleWord(true);
        DefaultCaret caret = (DefaultCaret) battleLogArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // 角色信息区
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        infoArea.setBackground(PANEL_BG);
        infoArea.setForeground(TEXT_COLOR);

        // 下拉框
        godCombo = createStyledCombo();
        titanCombo = createStyledCombo();
        for (Character g : gods) {
            godCombo.addItem(g.getName() + " - " + g.getTitle());
        }
        for (Character t : titans) {
            titanCombo.addItem(t.getName() + " - " + t.getTitle());
        }

        // 按钮
        startBtn = createStyledButton("开始战斗", ACCENT_COLOR);
        attackBtn = createStyledButton("普通攻击", new Color(200, 80, 80));
        skillBtn = createStyledButton("释放技能", new Color(100, 150, 255));
        defendBtn = createStyledButton("防御回复", new Color(80, 180, 130));
        fleeBtn = createStyledButton("逃跑", new Color(150, 150, 150));
        recordBtn = createStyledButton("战绩查询", new Color(180, 130, 200));
        clearBtn = createStyledButton("清空日志", new Color(120, 120, 140));

        // 战斗中按钮初始禁用
        setBattleButtonsEnabled(false);

        // 状态标签
        godHpLabel = createStyledLabel("神方HP: ---");
        titanHpLabel = createStyledLabel("泰坦HP: ---");
        roundLabel = createStyledLabel("回合: 0");
        statusLabel = createStyledLabel("请选择角色后点击[开始战斗]");
        statusLabel.setForeground(ACCENT_COLOR);

        // 欢迎信息
        appendToLog("============================================");
        appendToLog("    奥林匹斯诸神征讨泰坦 - 文字MUD战斗系统");
        appendToLog("============================================");
        appendToLog("  诸神与泰坦的终极之战即将打响...");
        appendToLog("  请在下方选择参战角色，点击[开始战斗]开始。");
        appendToLog("");
    }

    private JComboBox<String> createStyledCombo() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        combo.setBackground(PANEL_BG);
        combo.setForeground(TEXT_COLOR);
        combo.setPreferredSize(new Dimension(200, 28));
        return combo;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("微软雅黑", Font.BOLD, 12));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 32));
        return btn;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("微软雅黑", Font.BOLD, 13));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private void layoutComponents() {
        // ===== 顶部：标题栏 =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(BG_COLOR);
        JLabel titleLabel = new JLabel("奥林匹斯诸神征讨泰坦");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setForeground(ACCENT_COLOR);
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        // ===== 中间：左右分栏 =====
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(520);
        splitPane.setBackground(BG_COLOR);
        splitPane.setBorder(null);

        // 左侧：战斗日志
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(BG_COLOR);
        leftPanel.setBorder(createTitledBorder("战斗日志"));
        JScrollPane logScroll = new JScrollPane(battleLogArea);
        logScroll.setBorder(null);
        leftPanel.add(logScroll, BorderLayout.CENTER);

        // 右侧：角色信息
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(BG_COLOR);
        rightPanel.setBorder(createTitledBorder("角色信息"));
        JScrollPane infoScroll = new JScrollPane(infoArea);
        infoScroll.setBorder(null);
        rightPanel.add(infoScroll, BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        add(splitPane, BorderLayout.CENTER);

        // ===== 底部：控制面板 =====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 8, 8, 8));

        // 第一行：角色选择
        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 3));
        selectPanel.setBackground(BG_COLOR);
        selectPanel.add(createStyledLabel("神方:"));
        selectPanel.add(godCombo);
        selectPanel.add(createStyledLabel("  泰坦方:"));
        selectPanel.add(titanCombo);
        selectPanel.add(startBtn);
        bottomPanel.add(selectPanel);

        // 第二行：战斗状态
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 3));
        statusPanel.setBackground(BG_COLOR);
        statusPanel.add(godHpLabel);
        statusPanel.add(titanHpLabel);
        statusPanel.add(roundLabel);
        bottomPanel.add(statusPanel);

        // 第三行：战斗操作按钮
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 3));
        actionPanel.setBackground(BG_COLOR);
        actionPanel.add(attackBtn);
        actionPanel.add(skillBtn);
        actionPanel.add(defendBtn);
        actionPanel.add(fleeBtn);
        actionPanel.add(new JSeparator(SwingConstants.VERTICAL));
        actionPanel.add(recordBtn);
        actionPanel.add(clearBtn);
        bottomPanel.add(actionPanel);

        // 第四行：状态提示
        JPanel tipPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tipPanel.setBackground(BG_COLOR);
        tipPanel.add(statusLabel);
        bottomPanel.add(tipPanel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                title);
        border.setTitleFont(new Font("微软雅黑", Font.BOLD, 13));
        border.setTitleColor(ACCENT_COLOR);
        return border;
    }

    private void addEventListeners() {
        startBtn.addActionListener(e -> startBattle());
        attackBtn.addActionListener(e -> playerAction("attack"));
        skillBtn.addActionListener(e -> playerAction("skill"));
        defendBtn.addActionListener(e -> playerAction("defend"));
        fleeBtn.addActionListener(e -> playerAction("flee"));
        recordBtn.addActionListener(e -> showRecords());
        clearBtn.addActionListener(e -> {
            battleSystem.clearLog();
            battleLogArea.setText("");
            appendToLog("日志已清空。");
        });

        godCombo.addActionListener(e -> updateInfoArea());
        titanCombo.addActionListener(e -> updateInfoArea());
    }

    /** 开始战斗 */
    private void startBattle() {
        int gi = godCombo.getSelectedIndex();
        int ti = titanCombo.getSelectedIndex();
        if (gi < 0 || ti < 0) {
            JOptionPane.showMessageDialog(this, "请先选择神方和泰坦方角色！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 深拷贝角色，避免修改原始数据
        selectedGod = cloneCharacter(gods.get(gi));
        selectedTitan = cloneCharacter(titans.get(ti));
        roundCount = 0;
        battleEnded = false;

        appendToLog("============================================");
        appendToLog(String.format("战斗开始！%s（%s） VS %s（%s）",
                selectedGod.getName(), selectedGod.getTitle(),
                selectedTitan.getName(), selectedTitan.getTitle()));
        appendToLog("--------------------------------------------");
        appendToLog(String.format("  %s: HP %d/%d, ATK %d, DEF %d, 技能[%s](威力%d)",
                selectedGod.getName(), selectedGod.getHp(), selectedGod.getMaxHp(),
                selectedGod.getAttack(), selectedGod.getDefense(),
                selectedGod.getSkill(), selectedGod.getSkillDamage()));
        appendToLog(String.format("  %s: HP %d/%d, ATK %d, DEF %d, 技能[%s](威力%d)",
                selectedTitan.getName(), selectedTitan.getHp(), selectedTitan.getMaxHp(),
                selectedTitan.getAttack(), selectedTitan.getDefense(),
                selectedTitan.getSkill(), selectedTitan.getSkillDamage()));
        appendToLog("--------------------------------------------");
        appendToLog("  第1回合开始 - 请选择行动：");

        roundCount = 1;
        updateHpLabels();
        roundLabel.setText("回合: " + roundCount);
        statusLabel.setText("战斗进行中 - 选择你的行动");
        setBattleButtonsEnabled(true);
        startBtn.setEnabled(false);
        godCombo.setEnabled(false);
        titanCombo.setEnabled(false);
        updateInfoArea();
    }

    /** 玩家行动 */
    private void playerAction(String action) {
        if (battleEnded) return;

        appendToLog(String.format("--- 第%d回合 ---", roundCount));

        switch (action) {
            case "attack":
                battleSystem.normalAttack(selectedGod, selectedTitan);
                break;
            case "skill":
                battleSystem.skillAttack(selectedGod, selectedTitan);
                break;
            case "defend":
                battleSystem.defend(selectedGod);
                break;
            case "flee":
                appendToLog(String.format("  > %s 选择了逃跑！战斗结束。", selectedGod.getName()));
                endBattle(selectedTitan);
                return;
        }

        // 检查泰坦是否阵亡
        if (!selectedTitan.isAlive()) {
            appendToLog(String.format("  > %s 已被击败！", selectedTitan.getName()));
            endBattle(selectedGod);
            return;
        }

        // AI回合
        appendToLog("  [泰坦回合]");
        battleSystem.aiTurn(selectedTitan, selectedGod);

        // 检查神方是否阵亡
        if (!selectedGod.isAlive()) {
            appendToLog(String.format("  > %s 已被击败！", selectedGod.getName()));
            endBattle(selectedTitan);
            return;
        }

        roundCount++;
        roundLabel.setText("回合: " + roundCount);
        updateHpLabels();
        appendToLog("  请选择行动：");
        refreshLog();
    }

    /** 战斗结束 */
    private void endBattle(Character winner) {
        battleEnded = true;
        setBattleButtonsEnabled(false);
        startBtn.setEnabled(true);
        godCombo.setEnabled(true);
        titanCombo.setEnabled(true);

        String summary = winner.getName() + " 获得胜利！";
        appendToLog("============================================");
        appendToLog("  战斗结束！" + summary);

        // 战利品
        Character loser = (winner == selectedGod) ? selectedTitan : selectedGod;
        battleSystem.generateLoot(winner, loser);

        // 记录战绩
        BattleRecord record = battleSystem.recordBattle(selectedGod, selectedTitan, roundCount, summary);
        appendToLog(String.format("  战绩已记录: %s", record));
        appendToLog("============================================");
        appendToLog("  可选择新角色开始下一场战斗。");
        appendToLog("");

        statusLabel.setText("战斗结束 - " + winner.getName() + " 获胜！可开始新战斗");
        updateHpLabels();
        refreshLog();
    }

    /** 显示战绩 */
    private void showRecords() {
        List<BattleRecord> records = battleSystem.getBattleRecords();
        if (records.isEmpty()) {
            JOptionPane.showMessageDialog(this, "暂无战斗记录。", "战绩查询", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("===== 战绩统计 =====\n");
        sb.append(String.format("总战斗场次: %d\n\n", records.size()));

        int godWins = 0, titanWins = 0;
        for (BattleRecord r : records) {
            sb.append(r.toString()).append("\n");
            if (r.getGodName().equals(r.getWinner())) {
                godWins++;
            } else {
                titanWins++;
            }
        }
        sb.append(String.format("\n奥林匹斯胜场: %d  |  泰坦胜场: %d\n", godWins, titanWins));
        sb.append(String.format("奥林匹斯胜率: %.1f%%\n", godWins * 100.0 / records.size()));

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 350));

        JOptionPane.showMessageDialog(this, scrollPane, "战绩查询",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /** 更新角色信息区 */
    private void updateInfoArea() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== 奥林匹斯诸神 =====\n");
        for (int i = 0; i < gods.size(); i++) {
            Character g = gods.get(i);
            sb.append(String.format("[%d] %s\n", i + 1, g.getBriefInfo()));
            sb.append(String.format("     技能: %s (威力:%d)\n\n", g.getSkill(), g.getSkillDamage()));
        }
        sb.append("\n===== 泰坦残部 =====\n");
        for (int i = 0; i < titans.size(); i++) {
            Character t = titans.get(i);
            sb.append(String.format("[%d] %s\n", i + 1, t.getBriefInfo()));
            sb.append(String.format("     技能: %s (威力:%d)\n\n", t.getSkill(), t.getSkillDamage()));
        }
        infoArea.setText(sb.toString());
    }

    private void updateHpLabels() {
        if (selectedGod != null) {
            godHpLabel.setText(String.format("%s HP: %d/%d",
                    selectedGod.getName(), selectedGod.getHp(), selectedGod.getMaxHp()));
        }
        if (selectedTitan != null) {
            titanHpLabel.setText(String.format("%s HP: %d/%d",
                    selectedTitan.getName(), selectedTitan.getHp(), selectedTitan.getMaxHp()));
        }
    }

    private void setBattleButtonsEnabled(boolean enabled) {
        attackBtn.setEnabled(enabled);
        skillBtn.setEnabled(enabled);
        defendBtn.setEnabled(enabled);
        fleeBtn.setEnabled(enabled);
    }

    private void appendToLog(String msg) {
        battleLogArea.append(msg + "\n");
    }

    private void refreshLog() {
        battleLogArea.append(battleSystem.getLog());
        battleSystem.clearLog();
    }

    /** 深拷贝角色（保证原始列表不被修改） */
    private Character cloneCharacter(Character src) {
        return new Character(src.getName(), src.getTitle(), src.getFaction(),
                src.getMaxHp(), src.getAttack(), src.getDefense(),
                src.getSkill(), src.getSkillDamage());
    }

    public static void main(String[] args) {
        // 设置跨平台外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new OlympusBattleGUI();
        });
    }
}
