import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArlSdHxWjfSbGvHWkhcryEyS9hdresAc6UnA9vazAjgj4tounQ9c2ddnMv0IwS2I6atxF2P4n8j3ged9jZ1AfKMubbBbDjmbJIecQNWw0a5l8ffymp/y9PGuBjEiD4tMIU3zwT/0/RvO2pvUSvguWSzArlLvWDBQL90pfwf+eKaoDGW4G0FN0DuPhkrz1jJNUFRBKyxvUyoJL6TaHsLTnhf2mF4XX599kCDtLNpcB0zniZxwPT+AjpT4fRSj+EB2Z5U/pnTwUTe5f1jnzok1iBoFBxtbvOSXKljZU1hhyxAxMFXHLTLWKz1Jot46MqIYlem0hjXigME3fyOCYDIaSqwIDAQAB'

export default {

  // 加密
  encrypt(txt) {
    const encryptor = new JSEncrypt()
    encryptor.setPublicKey(publicKey) // 设置公钥
    return encryptor.encrypt(txt) // 对数据进行加密
  },

  // 解密
  decrypt(txt) {
    const encryptor = new JSEncrypt()
    encryptor.setPrivateKey(publicKey) // 设置私钥
    return encryptor.decrypt(txt) // 对数据进行解密
  }

}
