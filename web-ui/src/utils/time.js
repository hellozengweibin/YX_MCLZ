
// 大于当前月分的日期不可选
export function banTime() {
  return {
    disabledDate(time) {
      const date = new Date()
      const year = date.getFullYear()
      let month = date.getMonth() + 1
      if (month >= 1 && month <= 9) {
        month = '0' + month
      }
      const currentdate = year.toString() + month.toString()
      const timeyear = time.getFullYear()
      let timemonth = time.getMonth() + 1
      if (timemonth >= 1 && timemonth <= 9) {
        timemonth = '0' + timemonth
      }
      const timedate = timeyear.toString() + timemonth.toString()
      return currentdate < timedate
    }
  }
}
