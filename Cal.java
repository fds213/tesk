import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *version 2.0
 *"Icon.png" 아이콘 사진이 src폴더에 없을경우 오류!!
 *
 * @created 2024-10-15
 * @lastModified 2024-10-31
 *
 * @changelog
 * <ul>
 *  <li>2024-10-15: 최초 생성 (fds213)</li>
 *  <li>2024-10-19: 계산기 UI개선</li>
 * <li>2024-10-21: 계산기 아이콘 추가</li>
 * <li>2024-10-30: 버튼 입력 및 계산 기능 추가</li>
 * <li>2024-10-31: 연산자 %추가, +/-추가</li>
 * </ul>
 */
public class Cal extends JFrame {
    // GUI 구성 요소
    JTextField text1, text2; // 입력 및 결과 창
    JPanel bp, bh; // 버튼 및 화면 패널
    String[] num = {
            "%", "C", "÷", "⌫",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "+/-", "0", ".", "="
    };
    String currentExpression = ""; // 현재 입력된 계산식
    boolean isResultDisplayed = false; // 결과가 표시 중인지 여부

    /**
     * 계산기 UI를 초기화하는 생성자.
     */
    public Cal() {
        setTitle("계산기"); // 창 제목 설정
        setSize(330, 500); // 창 크기 설정
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 닫기 버튼 설정
        setLayout(new BorderLayout());

        // 현재 입력/결과를 표시하는 텍스트 필드
        text1 = new JTextField("0");
        text1.setHorizontalAlignment(JTextField.RIGHT); // 오른쪽 정렬
        text1.setEnabled(false); // 사용자 입력 비활성화
        text1.setFont(new Font("Arial", Font.PLAIN, 50));
        text1.setBackground(new Color(51, 51, 51)); // 배경색 설정
        text1.setForeground(Color.WHITE); // 글자색 설정
        text1.setBorder(null);

        // 이전 입력/식 표현을 위한 텍스트 필드
        text2 = new JTextField("");
        text2.setHorizontalAlignment(JTextField.RIGHT); // 오른쪽 정렬
        text2.setEnabled(false); // 사용자 입력 비활성화
        text2.setFont(new Font("Arial", Font.PLAIN, 30));
        text2.setBackground(new Color(51, 51, 51));
        text2.setForeground(Color.WHITE);
        text2.setBorder(null);

        // 화면에 표시되는 패널
        bh = new JPanel(new BorderLayout());
        bh.add(text1, BorderLayout.CENTER);
        bh.add(text2, BorderLayout.NORTH);
        add(bh, BorderLayout.NORTH);

        // 버튼 패널 설정
        bp = new JPanel();
        bp.setLayout(new GridLayout(5, 4, 1, 1));
        add(bp, BorderLayout.CENTER);

        // 버튼 생성 및 이벤트 리스너 추가
        for (String s : num) {
            JButton button = new JButton(s);
            button.setBackground(Color.darkGray); // 버튼 배경색
            button.setForeground(Color.white); // 버튼 글자색
            button.addActionListener(new ButtonClickListener(s)); // 클릭 이벤트 추가
            bp.add(button);
        }

        // 아이콘 설정 (Icon.png가 src폴더 안에 있어야됨!!)
        setIconImage(new ImageIcon(getClass().getResource("/Icon.png")).getImage());

        setVisible(true); // 창 보이기 설정
    }

    /**
     * 메인 메서드. 프로그램 시작 지점.
     */
    public static void main(String[] args) {
        new Cal(); // 계산기 창 생성
    }

    /**
     * 주어진 계산식(expression)을 계산하고 결과를 반환함.
     * @param expression 계산할 문자열 표현식
     * @return 계산 결과
     * @see <a href="https://www.openai.com/research/chatgpt">OpenAI ChatGPT</a>
     */
    private double calculate(String expression) {
        String replacedExpression = expression.replace("×", "*").replace("÷", "/"); // 기호 변환
        String[] tokens = replacedExpression.split("(?<=[-+*/])|(?=[-+*/])"); // 연산자 기준으로 토큰화
        double result = 0;
        double currentValue = 0;
        String currentOperator = "+";

        // 토큰을 순회하며 계산 수행
        for (String token : tokens) {
            if (token.matches("[-+]?\\d*\\.?\\d+")) {
                currentValue = Double.parseDouble(token);

                // 연산자에 따라 계산 수행
                switch (currentOperator) {
                    case "+":
                        result += currentValue;
                        break;
                    case "-":
                        result -= currentValue;
                        break;
                    case "*":
                        result *= currentValue;
                        break;
                    case "/":
                        result /= currentValue;
                        break;
                }
            } else {
                currentOperator = token; // 현재 연산자 설정
            }
        }
        return result;
    }

    /**
     * 결과를 포맷팅하여 문자열로 반환함.
     * 정수일 경우 소수점 제거.
     * @param result 포맷팅할 결과 값
     * @return 포맷팅된 문자열
     */
    private String formatResult(double result) {
        if (result % 1 == 0) {
            return String.valueOf((int) result);
        } else {
            return String.valueOf(result).replaceAll("\\.0*$", "");
        }
    }

    /**
     * 버튼 클릭 이벤트 리스너 클래스.
     * 버튼 텍스트에 따라 계산기 동작을 수행함.
     */
    private class ButtonClickListener implements ActionListener {
        String buttonText;

        public ButtonClickListener(String text) {
            this.buttonText = text;
        }

        public void actionPerformed(ActionEvent e) {
            switch (buttonText) {
                case "+/-":
                    // 현재 값의 부호를 전환
                    if (!currentExpression.isEmpty()) {
                        if (currentExpression.charAt(0) == '-') {
                            currentExpression = currentExpression.substring(1);
                        } else {
                            currentExpression = "-" + currentExpression;
                        }
                        text1.setText(currentExpression);
                    }
                    break;
                case "C":
                    // 전체 초기화
                    currentExpression = "";
                    text1.setText("0");
                    text2.setText("");
                    isResultDisplayed = false;
                    break;
                case "⌫":
                    // 마지막 문자 삭제
                    if (currentExpression.length() > 0) {
                        currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
                        text1.setText(currentExpression.isEmpty() ? "0" : currentExpression);
                    }
                    break;
                case "=":
                    // 계산 수행 및 결과 표시
                    try {
                        text2.setText(currentExpression);
                        double result = calculate(currentExpression);
                        text1.setText(formatResult(result));
                        currentExpression = String.valueOf(result);
                        isResultDisplayed = true;
                    } catch (Exception ex) {
                        text1.setText("Error");
                    }
                    break;
                case "%":
                    // 백분율 계산
                    try {
                        if (!currentExpression.isEmpty()) {
                            double value = Double.parseDouble(currentExpression);
                            double percentValue = value / 100;
                            text1.setText(formatResult(percentValue));
                            currentExpression = String.valueOf(percentValue);
                        }
                    } catch (NumberFormatException ex) {
                        text1.setText("Error");
                    }
                    break;
                case "×": case "÷": case "+": case "-":
                    // 연산자 추가
                    if (!currentExpression.isEmpty() && !isOperator(currentExpression.charAt(currentExpression.length() - 1))) {
                        currentExpression += buttonText;
                        text1.setText(currentExpression);
                        isResultDisplayed = false;
                    }
                    break;
                case ".":
                    // 소수점 추가
                    if (!currentExpression.isEmpty() && !currentExpression.endsWith(".") && !isOperator(currentExpression.charAt(currentExpression.length() - 1))) {
                        if (!currentExpression.contains(".")) {
                            currentExpression += buttonText;
                            text1.setText(currentExpression);
                        }
                    }
                    break;
                default:
                    // 숫자 입력 처리
                    if (isResultDisplayed) {
                        currentExpression = buttonText;
                        text1.setText(buttonText);
                        isResultDisplayed = false;
                    } else {
                        if (text1.getText().equals("0")) {
                            text1.setText(buttonText);
                            currentExpression = buttonText;
                        } else {
                            currentExpression += buttonText;
                            text1.setText(currentExpression);
                        }
                    }
                    break;
            }
        }

        /**
         * 입력된 문자가 연산자인지 확인하는 메서드.
         * @param c 확인할 문자
         * @return 연산자 여부
         */
        private boolean isOperator(char c) {
            return c == '+' || c == '-' || c == '×' || c == '÷';
        }
    }
}
