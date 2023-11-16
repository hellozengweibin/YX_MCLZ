/**
 * 校验
 */
// 身份证
export const checkIdNum = (rule, value, callback) => {
  let pass = true
  const city = {
    11: '北京',
    12: '天津',
    13: '河北',
    14: '山西',
    15: '内蒙古',
    21: '辽宁',
    22: '吉林',
    23: '黑龙江',
    31: '上海',
    32: '江苏',
    33: '浙江',
    34: '安徽',
    35: '福建',
    36: '江西',
    37: '山东',
    41: '河南',
    42: '湖北',
    43: '湖南',
    44: '广东',
    45: '广西',
    46: '海南',
    50: '重庆',
    51: '四川',
    52: '贵州',
    53: '云南',
    54: '西藏',
    61: '陕西',
    62: '甘肃',
    63: '青海',
    64: '宁夏',
    65: '新疆',
    71: '台湾',
    81: '香港',
    82: '澳门',
    91: '国外'
  }
  if (!value || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(value)) {
    callback(new Error('身份证号格式错误'))
    pass = false
  } else if (!city[value.substr(0, 2)]) {
    callback(new Error('地址编码错误'))
    pass = false
  } else {
    if (value.length === 18) {
      value = value.split('')
      const factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2]
      const parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2]
      let sum = 0
      let ai = 0
      let wi = 0
      for (let i = 0; i < 17; i++) {
        ai = value[i]
        wi = factor[i]
        sum += ai * wi
      }
      if (`${parity[sum % 11]}` !== value[17]) {
        callback(new Error('校验位错误'))
        pass = false
      }
    }
  }
  if (pass) {
    callback()
  }
}

// 手机号码
export const checkPhone = (rule, value, callback) => {
  const phoneReg = /^(13[0-9]|14[01456789]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/
  if (!value) {
    return callback(new Error('手机号码不能为空'))
  } else if (phoneReg.test(value)) {
    callback()
  } else {
    return callback(new Error('手机号码格式不正确'))
  }
}

// 姓名
export const checkName = (rule, value, callback) => {
  const name = /^[\u4E00-\u9FA5A-Za-z\uf900-\ufa2d·s]{2,10}$/
  if (!value) {
    return callback(new Error('姓名不能为空'))
  }
  if (!name.test(value)) {
    callback(new Error('请输入正确姓名!'))
  } else {
    callback()
  }
}

// 用户名称
export const checkUserName = (rule, value, callback) => {
  if (!value) {
    return callback(new Error('用户名称不能为空'))
  }
  if (value.length < 2 || value.length > 20) {
    return callback(new Error('用户名称长度2-20位'))
  }
  var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>《》/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]")
  if (pattern.test(value)) {
    return callback(new Error('用户名称不能包含特殊字符'))
  }

  if (/.*[\u4e00-\u9fa5]+.*$/.test(value)) {
    return callback(new Error('用户名称不能包含汉字'))
  }

  return callback()
}

export const checkPwd = (rule, value, callback) => {
  if (!value) {
    return callback(new Error('密码不能为空'))
  }
  if (value.length < 8 || value.length > 16) {
    return callback(new Error('密码长度8-16位'))
  }
  // 是数字
  var isDigit = /^.*[0-9]+.*/

  // isLowerCase 小写字母
  var isLowerCase = /^.*[a-z]+.*/

  // isUpperCase 大写字母
  var isUpperCase = /^.*[A-Z]+.*/

  // 特殊字符
  var regEx = /^.*[^a-zA-Z0-9]+.*/

  var passLower = value.toLowerCase()

  // if(!isLowerCase.test(value.substring(0, 1)) && !isUpperCase.test(value.substring(0, 1))){
  //   return callback(new Error('密码必须以英文字母开头'))
  // }
  // 记录匹配的次数
  var num = 0
  if (isDigit.test(value)) {
    num = num + 1
  }
  if (isLowerCase.test(value) || isUpperCase.test(value)) {
    num = num + 1
  }
  if (regEx.test(value)) {
    num = num + 1
  }
  if (num <= 2) {
    return callback(new Error('密码应包括大小写字母、数字、特殊字符3类'))
  }

  // 密码中不能包含有连续四位及以上顺序(或逆序)数字(如：密码中不能包含1234或3210等)。
  var numberSequence = false
  for (var i = 0; i < value.length - 3; i++) {
    if (!isDigit.test(value.substring(i, i + 1)) || !isDigit.test(value.substring(i + 1, i + 2) + '') || !isDigit.test(value.substring(i + 2, i + 3) + '') || !isDigit.test(value.substring(i + 3, i + 4) + '')) {
      continue
    }
    var no1 = Number(value.substring(i, i + 1))
    var no2 = Number(value.substring(i + 1, i + 2))
    var no3 = Number(value.substring(i + 2, i + 3))
    var no4 = Number(value.substring(i + 3, i + 4))
    if (no1 + 1 === no2 && no2 + 1 === no3 && no3 + 1 === no4) {
      numberSequence = true
    }
    if (no2 + 1 === no1 && no3 + 1 === no2 && no4 + 1 === no3) {
      numberSequence = true
    }
  }
  if (numberSequence) {
    // $.messager.alert("错误","密码中不能包含有连续四位及以上顺序(或逆序)数字(如：密码中不能包含1234或3210等)。");
    return callback(new Error('密码中不能包含有连续四位及以上顺序(或逆序)数字(如：密码中不能包含1234或3210等'))
  }
  // 密码中不能包含有连续四位及以上顺序(或逆序)字母，字母不区分大小写(如：密码中不能包含abcd或ABCD或AbcD或DcbA等)。
  var letterSequence = false
  // eslint-disable-next-line no-redeclare
  for (var i = 0; i < passLower.length - 3; i++) {
    if (!isLowerCase.test(passLower.substring(i, i + 1)) || !isLowerCase.test(passLower.substring(i + 1, i + 2) + '') || !isLowerCase.test(passLower.substring(i + 2, i + 3) + '') || !isLowerCase.test(passLower.substring(i + 3, i + 4) + '')) {
      continue
    }
    // eslint-disable-next-line no-redeclare
    var no1 = passLower.charAt(i).charCodeAt() + 0
    // eslint-disable-next-line no-redeclare
    var no2 = passLower.charAt(i + 1).charCodeAt() + 0
    // eslint-disable-next-line no-redeclare
    var no3 = passLower.charAt(i + 2).charCodeAt() + 0
    // eslint-disable-next-line no-redeclare
    var no4 = passLower.charAt(i + 3).charCodeAt() + 0
    if (no1 + 1 === no2 && no2 + 1 === no3 && no3 + 1 === no4) {
      letterSequence = true
    }
    if (no2 + 1 === no1 && no3 + 1 === no2 && no4 + 1 === no3) {
      letterSequence = true
    }
  }
  if (letterSequence) {
    return callback(new Error('密码中不能包含有连续四位及以上顺序(或逆序)字母，字母不区分大小写(如：密码中不能包含abcd或ABCD或AbcD或AbcD或DcbA等'))
  }
  var fourOrMoreSame = false
  // eslint-disable-next-line no-redeclare
  for (var i = 0; i < value.length - 3; i++) {
    // eslint-disable-next-line eqeqeq
    if ((passLower.charAt(i)) == passLower.charAt(i + 1) &&
      passLower.charAt(i + 1) === passLower.charAt(i + 2) &&
      passLower.charAt(i + 2) === passLower.charAt(i + 3)) {
      fourOrMoreSame = true
    }
  }
  if (fourOrMoreSame) {
    return callback(new Error('密码中不能包含有连续四位及以上重复字符，字母不区分大小写'))
  }
  var cont = /^.*admin.*$/
  var cont2 = /^.*pass.*$/
  if (cont.test(passLower) || cont2.test(passLower)) {
    return callback(new Error('禁忌词不区分大小写不能作为密码的一部分存在于密码中(如：admin, pass)'))
  }
  return callback()
}

export const checkPwdNoBack = (value, userName) => {
  var flag = ' '
  if (!value) {
    flag = '密码不能为空'
    return flag
  }
  if ((value.length < 8 || value.length > 16)) {
    flag = '密码长度8-16位'
    return flag
  }
  // 是数字
  var isDigit = /^.*[0-9]+.*/

  // isLowerCase 小写字母
  var isLowerCase = /^.*[a-z]+.*/

  // isUpperCase 大写字母
  var isUpperCase = /^.*[A-Z]+.*/

  // 特殊字符
  var regEx = /^.*[^a-zA-Z0-9]+.*/

  var passLower = value.toLowerCase()

  // if(!isLowerCase.test(value.substring(0, 1)) && !isUpperCase.test(value.substring(0, 1))){
  //   flag = '密码必须以英文字母开头'
  //   return flag
  // }
  // 记录匹配的次数
  var num = 0
  if (isDigit.test(value)) {
    num = num + 1
  }
  if (isLowerCase.test(value) || isUpperCase.test(value)) {
    num = num + 1
  }

  if (regEx.test(value)) {
    num = num + 1
  }
  if (num <= 2) {
    flag = '密码应包括大小写字母、数字、特殊字符3类'
    return flag
  }

  // 新密码中不能够包含帐户。
  var userNameLower = userName.toLowerCase()
  if (passLower.indexOf(userNameLower) >= 0) {
    return '新密码不能包含用户名'
  }

  // 密码中不能包含有连续四位及以上顺序(或逆序)数字(如：密码中不能包含1234或3210等)。
  var numberSequence = false
  for (var i = 0; i < value.length - 3; i++) {
    if (!isDigit.test(value.substring(i, i + 1)) || !isDigit.test(value.substring(i + 1, i + 2) + '') || !isDigit.test(value.substring(i + 2, i + 3) + '') || !isDigit.test(value.substring(i + 3, i + 4) + '')) {
      continue
    }
    var no1 = Number(value.substring(i, i + 1))
    var no2 = Number(value.substring(i + 1, i + 2))
    var no3 = Number(value.substring(i + 2, i + 3))
    var no4 = Number(value.substring(i + 3, i + 4))
    if (no1 + 1 === no2 && no2 + 1 === no3 && no3 + 1 === no4) {
      numberSequence = true
    }
    if (no2 + 1 === no1 && no3 + 1 === no2 && no4 + 1 === no3) {
      numberSequence = true
    }
  }
  if (numberSequence) {
    flag = '密码中不能包含有连续四位及以上顺序(或逆序)数字(如：密码中不能包含1234或3210等'
    return flag
  }
  // 密码中不能包含有连续四位及以上顺序(或逆序)字母，字母不区分大小写(如：密码中不能包含abcd或ABCD或AbcD或DcbA等)。
  var letterSequence = false
  // eslint-disable-next-line no-redeclare
  for (var i = 0; i < passLower.length - 3; i++) {
    if (!isLowerCase.test(passLower.substring(i, i + 1)) || !isLowerCase.test(passLower.substring(i + 1, i + 2) + '') || !isLowerCase.test(passLower.substring(i + 2, i + 3) + '') || !isLowerCase.test(passLower.substring(i + 3, i + 4) + '')) {
      continue
    }
    // eslint-disable-next-line no-redeclare
    var no1 = passLower.charAt(i).charCodeAt() + 0
    // eslint-disable-next-line no-redeclare
    var no2 = passLower.charAt(i + 1).charCodeAt() + 0
    // eslint-disable-next-line no-redeclare
    var no3 = passLower.charAt(i + 2).charCodeAt() + 0
    // eslint-disable-next-line no-redeclare
    var no4 = passLower.charAt(i + 3).charCodeAt() + 0
    if (no1 + 1 === no2 && no2 + 1 === no3 && no3 + 1 === no4) {
      letterSequence = true
    }
    if (no2 + 1 === no1 && no3 + 1 === no2 && no4 + 1 === no3) {
      letterSequence = true
    }
  }
  if (letterSequence) {
    flag = '密码中不能包含有连续四位及以上顺序(或逆序)字母，字母不区分大小写(如：密码中不能包含abcd或ABCD或AbcD或AbcD或DcbA等'
    return flag
  }
  var fourOrMoreSame = false
  // eslint-disable-next-line no-redeclare
  for (var i = 0; i < value.length - 3; i++) {
    // eslint-disable-next-line eqeqeq
    if ((passLower.charAt(i)) == passLower.charAt(i + 1) &&
      passLower.charAt(i + 1) === passLower.charAt(i + 2) &&
      passLower.charAt(i + 2) === passLower.charAt(i + 3)) {
      fourOrMoreSame = true
    }
  }
  if (fourOrMoreSame) {
    flag = '密码中不能包含有连续四位及以上重复字符，字母不区分大小写'
    return flag
  }
  var cont = /^.*admin.*$/
  var cont2 = /^.*pass.*$/
  if (cont.test(passLower) || cont2.test(passLower)) {
    flag = '禁忌词不区分大小写不能作为密码的一部分存在于密码中(如：admin, pass)'
    return flag
  }
  return 'true'
}

// 正则判空处理
export const checkTrim = (value, property) => {
  if (!value) {
    return property + '不能为空'
  }

  var reg = /(^\s+)|(\s+$)|\s+/g
  if (reg.test(value)) {
    return property + '不能出现空格'
  }
  return 'true'
}
