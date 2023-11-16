package com.eshore.common.utils;

import com.eshore.common.constant.UserConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 密码安全策略，杜绝弱口令
 */
public class PwdCheckUtil {
    // 斜向连续键盘数字
    public static String[] KEYBOARD_SLOPE_ARR = {
            "!qaz", "1qaz", "@wsx", "2wsx", "#edc", "3edc", "$rfv", "4rfv", "%tgb", "5tgb",
            "^yhn", "6yhn", "&ujm", "7ujm", "*ik,", "8ik,", "(ol.", "9ol.", ")p;/", "0p;/",
            "+[;.", "=[;.", "_pl,", "-pl,", ")okm", "0okm", "(ijn", "9ijn", "*uhb", "8uhb",
            "&ygv", "7ygv", "^tfc", "6tfc", "%rdx", "5rdx", "$esz", "4esz"
    };
    // 横向连续键盘数字
    public static String[] KEYBOARD_HORIZONTAL_ARR = {
            "01234567890-=",
            "!@#$%^&*()_+",
            "qwertyuiop[]",
            "QWERTYUIOP{}",
            "asdfghjkl;'",
            "ASDFGHJKL:",
            "zxcvbnm,./",
            "ZXCVBNM<>?",
    };

    // 特殊字符
    public static String SPECIAL_CHAR = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    //特殊形似字符
    private static String SPECIALSTR = "1Ll|iI!";

    // ! 形似 !i
    private static String similar_tanhao = "!i";
    // i 形似 iI!
    private static String similar_lower_i = "iI!";
    // I 形似 lL1I|i
    private static String similar_upper_i = "lL1I|i";
    // l 形似 lL1I|
    private static String similar_lower_l = "lL1I|";
    // L 形似 lL1I|
    private static String similar_upper_l = "lL1I|";
    // 1 形似 lL1I|
    private static String similar_number_1 = "lL1I|";
    // | 形似 lL1I|
    private static String similar_separator = "lL1I|";


    public static void main(String[] args) {

        String password = "Za33334";
//        System.out.println("密码长度校验："+checkPasswordLength(password, "8", "20"));
//        System.out.println("是否包含数字校验："+checkContainDigit(password));
//        System.out.println("是否包含字母（不分大小写）校验："+checkContainCase(password));
//        System.out.println("是否包含小写字母校验："+checkContainLowerCase(password));
//        System.out.println("是否包含大写字母校验："+checkContainUpperCase(password));
//        System.out.println("是否包含特殊字符校验："+checkContainSpecialChar(password));
//        System.out.println("不区分大小写横向键盘顺序连续输入4个及以上字符" + checkLateralKeyboardSite(password, 4, false));
//        System.out.println("不区分大小写斜像向键盘顺序连续输入4个及以上字符"+checkKeyboardSlantSite(password, 3,false));
//        System.out.println("不区分大小写评估a-z,z-a连续输入4个及以上字符"+checkSequentialChars(password, 4, false));
//        System.out.println("连续输入3个及以上重复字符"+checkSequentialSameChars(password, 4));
        System.out.println(checkPasswordSimilar("admin", "admin123"));
    }

    public static String checkPassword(String userName, String password) {

        if (!checkPasswordLength(password, UserConstants.PASSWORD_MIN_LENGTH, UserConstants.PASSWORD_MAX_LENGTH)) {
            return "密码长度8-16位";
        }

        if (!checkLeastThreeCategoryChar(password)) {
            return "密码应包括大小写字母、数字、特殊字符3类";
        }

        if (checkLateralKeyboardSite(password, 3, false) || checkKeyboardSlantSite(password, 3, false)) {
            return "密码中不能包含有连续三位及以上按键盘顺序密码(如：密码中不能包含qwe或qaz或!@#等)";
        }

        if (checkSequentialChars(password, 4, false)) {
            return "密码中不能包含有连续四位及以上顺序(或逆序)字符(如：密码中不能包含1234或3210、abcd或ABCD或AbcD或Abcd或DcbA等";
        }

        if (checkSequentialSameChars(password, 4)) {
            return "密码中不能包含有连续四位及以上重复字符，字母不区分大小写(如：密码中不能包含8888、aaaa或AAAA或BbBB或 $$$$等四位及以上的重复字符";
        }

        if (checkPasswordSimilar(userName, password)) {
            return "密码应与用户名无相关性，密码中不得包含用户名的完整字符串、大小写变位或形似变换的字符串";
        }

        return "1";
    }

    /**
     * @return 符合长度要求 返回true
     * @brief 检测密码中字符长度
     * @param[in] password      密码字符串
     */
    public static boolean checkPasswordLength(String password, int minNum, int maxNum) {
        if (password.length() >= minNum && password.length() <= maxNum) {
            return true;
        }
        return false;
    }

    /**
     * @return 包含数字 返回true
     * @brief 检测密码中是否包含数字
     * @param[in] password      密码字符串
     */
    public static boolean checkContainDigit(String password) {
        char[] chPass = password.toCharArray();
        boolean flag = false;
        int num_count = 0;

        for (int i = 0; i < chPass.length; i++) {
            if (Character.isDigit(chPass[i])) {
                num_count++;
            }
        }
        if (num_count >= 1) {
            flag = true;
        }
        return flag;
    }

    /**
     * @return 包含字母 返回true
     * @brief 检测密码中是否包含字母（不区分大小写）
     * @param[in] password      密码字符串
     */
    public static boolean checkContainCase(String password) {
        char[] chPass = password.toCharArray();
        boolean flag = false;
        int char_count = 0;

        for (int i = 0; i < chPass.length; i++) {
            if (Character.isLetter(chPass[i])) {
                char_count++;
            }
        }
        if (char_count >= 1) {
            flag = true;
        }
        return flag;
    }


    /**
     * @return 包含小写字母 返回true
     * @brief 检测密码中是否包含小写字母
     * @param[in] password      密码字符串
     */
    public static boolean checkContainLowerCase(String password) {
        char[] chPass = password.toCharArray();
        boolean flag = false;
        int char_count = 0;

        for (int i = 0; i < chPass.length; i++) {
            if (Character.isLowerCase(chPass[i])) {
                char_count++;
            }
        }
        if (char_count >= 1) {
            flag = true;
        }
        return flag;
    }


    /**
     * @return 包含大写字母 返回true
     * @brief 检测密码中是否包含大写字母
     * @param[in] password      密码字符串
     */
    public static boolean checkContainUpperCase(String password) {
        char[] chPass = password.toCharArray();
        boolean flag = false;
        int char_count = 0;

        for (int i = 0; i < chPass.length; i++) {
            if (Character.isUpperCase(chPass[i])) {
                char_count++;
            }
        }
        if (char_count >= 1) {
            flag = true;
        }
        return flag;
    }

    /**
     * @return 包含特殊符号 返回true
     * @brief 检测密码中是否包含特殊符号
     * @param[in] password      密码字符串
     */
    public static boolean checkContainSpecialChar(String password) {
        char[] chPass = password.toCharArray();
        boolean flag = false;
        int special_count = 0;

        for (int i = 0; i < chPass.length; i++) {
            if (SPECIAL_CHAR.indexOf(chPass[i]) != -1) {
                special_count++;
            }
        }

        if (special_count >= 1) {
            flag = true;
        }
        return flag;
    }

    /**
     * @return 含有横向连续字符串 返回true
     * @brief 键盘规则匹配器 横向连续检测
     * @param[in] password      密码字符串
     */
    public static boolean checkLateralKeyboardSite(String password, int repetitions, boolean isLower) {
        String t_password = new String(password);
        //将所有输入字符转为小写
        t_password = t_password.toLowerCase();
        int n = t_password.length();
        /**
         * 键盘横向规则检测
         */
        boolean flag = false;
        int arrLen = KEYBOARD_HORIZONTAL_ARR.length;
        int limit_num = repetitions;

        for (int i = 0; i + limit_num <= n; i++) {
            String str = t_password.substring(i, i + limit_num);
            String distinguishStr = password.substring(i, i + limit_num);

            for (int j = 0; j < arrLen; j++) {
                String configStr = KEYBOARD_HORIZONTAL_ARR[j];
                String revOrderStr = new StringBuffer(KEYBOARD_HORIZONTAL_ARR[j]).reverse().toString();

                //检测包含字母(区分大小写)
                if (isLower) {
                    //考虑 大写键盘匹配的情况
                    String UpperStr = KEYBOARD_HORIZONTAL_ARR[j].toUpperCase();
                    if ((configStr.indexOf(distinguishStr) != -1) || (UpperStr.indexOf(distinguishStr) != -1)) {
                        flag = true;
                        return flag;
                    }
                    //考虑逆序输入情况下 连续输入
                    String revUpperStr = new StringBuffer(UpperStr).reverse().toString();
                    if ((revOrderStr.indexOf(distinguishStr) != -1) || (revUpperStr.indexOf(distinguishStr) != -1)) {
                        flag = true;
                        return flag;
                    }
                } else {
                    if (configStr.indexOf(str) != -1) {
                        flag = true;
                        return flag;
                    }
                    //考虑逆序输入情况下 连续输入
                    if (revOrderStr.indexOf(str) != -1) {
                        flag = true;
                        return flag;
                    }
                }
            }
        }
        return flag;
    }


    /**
     * @param password    字符串
     * @param repetitions 重复次数
     * @param isLower     是否区分大小写 true:区分大小写， false:不区分大小写
     * @return boolean  如1qaz,4rfv, !qaz,@WDC,zaq1 返回true
     * @throws
     * @Title: checkKeyboardSlantSite
     * @Description: 物理键盘，斜向连接校验， 如1qaz,4rfv, !qaz,@WDC,zaq1 返回true
     */
    public static boolean checkKeyboardSlantSite(String password, int repetitions, boolean isLower) {
        String t_password = new String(password);
        t_password = t_password.toLowerCase();
        int n = t_password.length();
        /**
         * 键盘斜线方向规则检测
         */
        boolean flag = false;
        int arrLen = KEYBOARD_SLOPE_ARR.length;
        int limit_num = repetitions;

        for (int i = 0; i + limit_num <= n; i++) {
            String str = t_password.substring(i, i + limit_num);
            String distinguishStr = password.substring(i, i + limit_num);
            for (int j = 0; j < arrLen; j++) {
                String configStr = KEYBOARD_SLOPE_ARR[j];
                String revOrderStr = new StringBuffer(KEYBOARD_SLOPE_ARR[j]).reverse().toString();
                //检测包含字母(区分大小写)
                if (isLower) {
                    //考虑 大写键盘匹配的情况
                    String UpperStr = KEYBOARD_SLOPE_ARR[j].toUpperCase();
                    if ((configStr.indexOf(distinguishStr) != -1) || (UpperStr.indexOf(distinguishStr) != -1)) {
                        flag = true;
                        return flag;
                    }
                    //考虑逆序输入情况下 连续输入
                    String revUpperStr = new StringBuffer(UpperStr).reverse().toString();
                    if ((revOrderStr.indexOf(distinguishStr) != -1) || (revUpperStr.indexOf(distinguishStr) != -1)) {
                        flag = true;
                        return flag;
                    }
                } else {
                    if (configStr.indexOf(str) != -1) {
                        flag = true;
                        return flag;
                    }
                    //考虑逆序输入情况下 连续输入
                    if (revOrderStr.indexOf(str) != -1) {
                        flag = true;
                        return flag;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * @param password    字符串
     * @param repetitions 连续个数
     * @param isLower     是否区分大小写 true:区分大小写， false:不区分大小写
     * @return boolean  含有a-z,z-a连续字符串 返回true
     * @throws
     * @Title: checkSequentialChars
     * @Description: 评估a-z,z-a这样的连续字符,
     */
    public static boolean checkSequentialChars(String password, int repetitions, boolean isLower) {
        String t_password = new String(password);
        boolean flag = false;
        int limit_num = repetitions;
        int normal_count = 0;
        int reversed_count = 0;
        //检测包含字母(区分大小写)
        if (!isLower) {
            t_password = t_password.toLowerCase();
        }
        int n = t_password.length();
        char[] pwdCharArr = t_password.toCharArray();

        for (int i = 0; i + limit_num <= n; i++) {
            normal_count = 0;
            reversed_count = 0;
            for (int j = 0; j < limit_num - 1; j++) {
                if (pwdCharArr[i + j + 1] - pwdCharArr[i + j] == 1) {
                    normal_count++;
                    if (normal_count == limit_num - 1) {
                        return true;
                    }
                }

                if (pwdCharArr[i + j] - pwdCharArr[i + j + 1] == 1) {
                    reversed_count++;
                    if (reversed_count == limit_num - 1) {
                        return true;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 验证键盘上是否存在多个连续重复的字符， 如！！！！, qqqq, 1111, ====, AAAA
     *
     * @param password    字符串
     * @param repetitions 重复次数
     * @return boolean  存在多个连续重复的字符， 如！！！！, qqqq, 1111, ====, AAAA  返回true
     */
    public static boolean checkSequentialSameChars(String password, int repetitions) {
        String t_password = new String(password);
        int n = t_password.length();
        char[] pwdCharArr = t_password.toCharArray();
        boolean flag = false;
        int limit_num = repetitions;
        int count = 0;
        for (int i = 0; i + limit_num <= n; i++) {
            count = 0;
            for (int j = 0; j < limit_num - 1; j++) {
                if (pwdCharArr[i + j] == pwdCharArr[i + j + 1]) {
                    count++;
                    if (count == limit_num - 1) {
                        return true;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 校验密码必须为大写字母、小写字母、数字、特殊符号至少三种以上组成
     *
     * @param password
     * @return 含有大写字母、小写字母、数字、特殊符号至少三种组成  返回true
     */
    public static boolean checkLeastThreeCategoryChar(String password) {
        boolean flag = false;
        int num = 0;
        if (checkContainDigit(password)) {
            num++;
        }
        if (checkContainLowerCase(password)) {
            num++;
        }
        if (checkContainUpperCase(password)) {
            num++;
        }
        if (checkContainSpecialChar(password)) {
            num++;
        }
        if (num >= 3) {
            flag = true;
        }
        return flag;
    }


    /**
     * 检测密码中是否包含用户名 包含返回true
     *
     * @param password
     * @param userName
     * @return
     */
    public static boolean checkUserName(String userName, String password) {
        password = password.toLowerCase();
        userName = userName.toLowerCase();
        if (password.equals(userName) || password.contains(userName) || userName.contains(password)) {
            return true;
        }
        return false;
    }

    /**
     * 检查账号密码是否形似
     *
     * @param userName
     * @param password
     * @return 存在形似  返回true
     */
    public static boolean checkPasswordSimilar(String userName, String password) {

        System.out.println("用户名称：" + userName);
        System.out.println("用户密码：" + password);

        boolean flag = false;

        //如果密码长度小于用户名长度不进行相关性校验
        if (password.length() >= userName.length()) {
            flag = specialTrans(userName, password);
        }
        return flag;
    }

    /**
     * 密码的相似字符校验
     */
    private static boolean specialTrans(String userName, String password) {

        boolean flag = false;

        //对账号和密码进行普通的大小写转换（特殊形似字符不进行转换）
        userName = generalTrans(userName);
        password = generalTrans(password);

        //用户名长度
        int len = userName.length();
        //用户名第一个字符
        String str = userName.substring(0, 1);
        int indexStart = 0;
        List<String> list = new ArrayList<>();

        //通过首字母判断有几个存在形似的字符串
        if (SPECIALSTR.contains(str)) {
            switch (str) {
                case "i":
                    //形似 i ! I
                    for (int i = 0; i < similar_lower_i.length(); i++) {
                        passwordSplit(password, similar_lower_i.substring(i, i + 1), indexStart, len, list);
                    }
                    break;
                case "!":
                    //形似 ! i
                    for (int i = 0; i < similar_tanhao.length(); i++) {
                        passwordSplit(password, similar_tanhao.substring(i, i + 1), indexStart, len, list);
                    }
                    break;
                case "I":
                    //形似  l L 1 I | i
                    for (int i = 0; i < similar_upper_i.length(); i++) {
                        passwordSplit(password, similar_upper_i.substring(i, i + 1), indexStart, len, list);
                    }
                    break;
                case "l":
                    //形似  l L 1 I |
                    for (int i = 0; i < similar_lower_l.length(); i++) {
                        passwordSplit(password, similar_lower_l.substring(i, i + 1), indexStart, len, list);
                    }
                    break;
                case "L":
                    //形似  l L 1 I |
                    for (int i = 0; i < similar_upper_l.length(); i++) {
                        passwordSplit(password, similar_upper_l.substring(i, i + 1), indexStart, len, list);
                    }
                    break;
                case "1":
                    //形似  l L 1 I |
                    for (int i = 0; i < similar_number_1.length(); i++) {
                        passwordSplit(password, similar_number_1.substring(i, i + 1), indexStart, len, list);
                    }
                    break;
                case "|":
                    //形似  l L 1 I |
                    for (int i = 0; i < similar_separator.length(); i++) {
                        passwordSplit(password, similar_separator.substring(i, i + 1), indexStart, len, list);
                    }
                    break;
                default:
                    break;
            }
        } else {

            //判断userName在password中存在几个可能的形似
            passwordSplit(password, str, indexStart, len, list);
        }

        System.out.println("密码多次拆分对比：" + list);
        String tmp = "";

        //循环拆分出来的list
        for (int i = 0; i < list.size(); i++) {
            //判断拆分出来的字符串数组，分别跟userName对比
            tmp = list.get(i);

            //从userName的第二个字符开始对比
            for (int j = 1; j < userName.length(); j++) {
                String char1 = userName.substring(j, j + 1);

                String char2 = tmp.substring(j, j + 1);

                //如果字符是特殊形似字符
                if (SPECIALSTR.contains(char1)) {
                    if ("i".equals(char1)) {
                        if (!similar_lower_i.contains(char2)) {
                            break;
                        }
                    } else if ("!".equals(char1)) {
                        if (!similar_tanhao.contains(char2)) {
                            break;
                        }
                    } else if ("I".equals(char1)) {
                        if (!similar_upper_i.contains(char2)) {
                            break;
                        }
                    } else if ("l".equals(char1)) {
                        if (!similar_lower_l.contains(char2)) {
                            break;
                        }
                    } else if ("L".equals(char1)) {
                        if (!similar_upper_l.contains(char2)) {
                            break;
                        }
                    } else if ("1".equals(char1)) {
                        if (!similar_number_1.contains(char2)) {
                            break;
                        }
                    } else if ("|".equals(char1)) {
                        if (!similar_separator.contains(char2)) {
                            break;
                        }
                    }
                } else if (!char1.equals(char2)) {
                    break;
                }

                //如果字符都一样
                if (j == userName.length() - 1) {
                    System.out.println("形似部分：" + tmp);
                    flag = true;
                }
            }
        }

        return flag;
    }

    /**
     * 将密码分段进行校验
     */
    private static void passwordSplit(String password, String str, int indexStart, int len, List<String> list) {
        while (true) {
            int tm = password.indexOf(str, indexStart);
            if (tm >= 0) {
                //如果首字符的位置+userName的长度 大于 密码长度，说明不可能存在形似
                if ((tm + len) <= password.length()) {
                    list.add(password.substring(tm, tm + len));
                }
                indexStart = tm + str.length();
            } else {
                indexStart = 0;
                break;
            }
        }
    }

    /**
     * 对普通的字符进行大小写转换（大小写、a/A=@、Z/z=2、O/o=0、S/s=＄）
     */
    private static String generalTrans(String str) {

        //a
        str = str.replace("A", "a");
        str = str.replace("@", "a");

        //b
        str = str.replace("B", "b");

        //c
        str = str.replace("C", "c");

        //d
        str = str.replace("D", "d");

        //e
        str = str.replace("E", "e");

        //f
        str = str.replace("F", "f");

        //g
        str = str.replace("G", "g");

        //h
        str = str.replace("H", "h");

        //i 形似：! I

        //j
        str = str.replace("J", "j");

        //k
        str = str.replace("K", "k");

        //L形似：1 L l I |

        //m
        str = str.replace("M", "m");

        //n
        str = str.replace("N", "n");

        //o
        str = str.replace("O", "o");
        str = str.replace("0", "o");

        //p
        str = str.replace("P", "p");

        //r
        str = str.replace("R", "r");

        //s
        str = str.replace("S", "s");
        str = str.replace("$", "s");

        //t
        str = str.replace("T", "t");

        //u
        str = str.replace("U", "u");

        //v
        str = str.replace("V", "v");

        //w
        str = str.replace("W", "w");

        //x
        str = str.replace("x", "x");

        //y
        str = str.replace("Y", "y");

        //z
        str = str.replace("Z", "z");
        str = str.replace("2", "z");

        return str;
    }
}