import CryptoJS from 'crypto-js' // npm install crypto-js --save-dev

const iv = CryptoJS.enc.Utf8.parse('1234567890abcdef')

export default {
  // 加密
  encrypt(word, key) {
    if (!word) {
      return word
    }
    const keyEnc = CryptoJS.enc.Utf8.parse(key)
    const src = CryptoJS.enc.Utf8.parse(word)
    const encrypted = CryptoJS.AES.encrypt(src, keyEnc, {
      iv: iv,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7
    })
    return encrypted.toString()
  },
  // 解密
  decrypt(word, key) {
    if (!word) {
      return word
    }
    const keyEnc = CryptoJS.enc.Utf8.parse(key)
    const decrypt = CryptoJS.AES.decrypt(word, keyEnc, {
      iv: iv,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7
    })
    return decrypt.toString(CryptoJS.enc.Utf8)
  },
  // 生成AES 16位秘钥
  getKey() {
    const len = 16
    const chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz1234567890'
    const maxPos = chars.length
    let character = ''
    for (let i = 0; i < len; i++) {
      character += chars.charAt(Math.floor(Math.random() * maxPos))
    }
    return character
  },
  getUuid() {
    const s = []
    const hexDigits = '0123456789abcdef'
    for (let i = 0; i < 36; i++) {
      const indexStart = Math.floor(Math.random() * 0x10)
      s[i] = hexDigits.substring(indexStart, indexStart + 1)
    }
    s[14] = '4'
    const indexStart = (s[19] & 0x3) | 0x8
    s[19] = hexDigits.substring(indexStart, indexStart + 1)
    s[8] = s[13] = s[18] = s[23] = '-'
    return s.join('')
  }

}
