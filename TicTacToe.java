import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends JFrame implements ActionListener {
    private JButton[] buttons = new JButton[9];
    private String currentPlayer = "X";
    private boolean singlePlayer;
    private int[][] winCombos = {
        {0,1,2}, {3,4,5}, {6,7,8}, // rows
        {0,3,6}, {1,4,7}, {2,5,8}, // cols
        {0,4,8}, {2,4,6}           // diagonals
    };

    public TicTacToe() {
        setGameMode();
        setTitle("Tic Tac Toe");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3));
        initButtons();
        setVisible(true);
    }

    private void setGameMode() {
        String mode = JOptionPane.showInputDialog(this, "Enter 1 for Single Player, 2 for Two Players:");
        singlePlayer = "1".equals(mode);
    }

    private void initButtons() {
        for (int i = 0; i < 9; i++) {
            JButton b = new JButton("");
            b.setFont(new Font("Segoe UI", Font.BOLD, 60));
            b.setFocusPainted(false);
            b.setBackground(Color.WHITE);
            b.setForeground(Color.DARK_GRAY);
            b.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
            b.addActionListener(this);
            buttons[i] = b;
            add(b);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();
        if (!clicked.getText().equals("") || gameOver()) return;

        clicked.setText(currentPlayer);
        if (checkWin(currentPlayer)) {
            highlightWinningCombo(currentPlayer);
            showResult("Player " + currentPlayer + " wins!");
            return;
        } else if (isDraw()) {
            showResult("It's a draw!");
            return;
        }

        currentPlayer = currentPlayer.equals("X") ? "O" : "X";

        if (singlePlayer && currentPlayer.equals("O")) {
            makeAIMove();
        }
    }

    private void makeAIMove() {
        int bestScore = Integer.MIN_VALUE;
        int move = -1;

        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().equals("")) {
                buttons[i].setText("O");
                int score = minimax(0, false);
                buttons[i].setText("");
                if (score > bestScore) {
                    bestScore = score;
                    move = i;
                }
            }
        }

        buttons[move].setText("O");

        if (checkWin("O")) {
            highlightWinningCombo("O");
            showResult("AI wins!");
        } else if (isDraw()) {
            showResult("It's a draw!");
        } else {
            currentPlayer = "X";
        }
    }

    private int minimax(int depth, boolean isMaximizing) {
        if (checkWin("O")) return 10 - depth;
        if (checkWin("X")) return depth - 10;
        if (isDraw()) return 0;

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().equals("")) {
                buttons[i].setText(isMaximizing ? "O" : "X");
                int score = minimax(depth + 1, !isMaximizing);
                buttons[i].setText("");
                bestScore = isMaximizing ? Math.max(score, bestScore) : Math.min(score, bestScore);
            }
        }
        return bestScore;
    }

    private boolean checkWin(String player) {
        for (int[] combo : winCombos) {
            if (player.equals(buttons[combo[0]].getText()) &&
                player.equals(buttons[combo[1]].getText()) &&
                player.equals(buttons[combo[2]].getText())) {
                return true;
            }
        }
        return false;
    }

    private void highlightWinningCombo(String player) {
        for (int[] combo : winCombos) {
            if (player.equals(buttons[combo[0]].getText()) &&
                player.equals(buttons[combo[1]].getText()) &&
                player.equals(buttons[combo[2]].getText())) {
                for (int i : combo) {
                    buttons[i].setBackground(new Color(173, 255, 168)); // light green
                }
                break;
            }
        }
    }

    private boolean isDraw() {
        for (JButton b : buttons) {
            if (b.getText().equals("")) return false;
        }
        return true;
    }

    private boolean gameOver() {
        return checkWin("X") || checkWin("O") || isDraw();
    }

    private void showResult(String message) {
        JOptionPane.showMessageDialog(this, message);
        resetBoard();
    }

    private void resetBoard() {
        for (JButton b : buttons) {
            b.setText("");
            b.setBackground(Color.WHITE);
        }
        currentPlayer = "X";
        if (singlePlayer && currentPlayer.equals("O")) {
            makeAIMove();
        }
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}
