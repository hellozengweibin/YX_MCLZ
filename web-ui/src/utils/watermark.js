const watermark = {}
const idd = '1.234523841642.1234124163'
var _interval = null

const setWatermark = (str, font = '48px STSong') => {
  const id = idd
  if (document.getElementById(id) !== null) {
    document.body.removeChild(document.getElementById(id))
  }
  // 创建一个画布
  const can = document.createElement('canvas')
  // 设置画布的长宽
  can.width = 400
  can.height = 400
  const cans = can.getContext('2d')
  // 设置旋转角度
  cans.rotate(-30 * Math.PI / 180)
  cans.font = font
  // 设置填充绘画的颜色、渐变或者模式
  cans.fillStyle = 'rgba(255, 255, 255, 0.2)'
  // 设置文本内容的当前对齐方式
  cans.textAlign = 'center'
  // 设置在绘制文本时使用的当前文本基线
  cans.textBaseline = 'Middle'
  // 在画布上绘制填色的文本（输出的文本，开始绘制文本的X坐标位置，开始绘制文本的Y坐标位置）
  cans.fillText(str, can.width / 3, can.height / 2)
  const div = document.createElement('div')
  div.id = id
  div.style.pointerEvents = 'none'
  div.style.top = '20px'
  div.style.left = '0px'
  div.style.position = 'fixed'
  div.style.zIndex = '100000'
  div.style.width = document.documentElement.clientWidth + 'px'
  div.style.height = document.documentElement.clientHeight + 'px'
  div.style.background = 'url(' + can.toDataURL('image/png') + ') left top repeat'
  document.body.appendChild(div)
  return id
}

const setWatermarkById = (str, font, id, parentId) => {
  const parent = document.getElementById(parentId)
  if (document.getElementById(id) !== null) {
    parent && parent.removeChild(document.getElementById(id))
  }
  // 创建一个画布
  const can = document.createElement('canvas')
  const div = document.createElement('div')
  div.id = id
  div.style.pointerEvents = 'none'
  div.style.left = '0px'
  div.style.position = 'relative'
  div.style.zIndex = '100000'
  div.style.width = parent.clientWidth + 'px'
  div.style.height = parent.clientHeight + 'px'

  // 设置画布的长宽
  can.width = parent.clientWidth / 2
  can.height = parent.clientHeight / 2
  const cans = can.getContext('2d')
  // 设置旋转角度
  cans.rotate(-30 * Math.PI / 180)
  cans.font = font
  // 设置填充绘画的颜色、渐变或者模式
  cans.fillStyle = 'rgba(255, 255, 255, 0.2)'
  // 设置文本内容的当前对齐方式
  cans.textAlign = 'center'
  // 设置在绘制文本时使用的当前文本基线
  cans.textBaseline = 'Middle'
  // 在画布上绘制填色的文本（输出的文本，开始绘制文本的X坐标位置，开始绘制文本的Y坐标位置）
  cans.fillText(str, can.width / 4, can.height * 3 / 4)

  div.style.background = 'url(' + can.toDataURL('image/png') + ') left top repeat'
  parent.appendChild(div)
  return id
}

// 该方法只允许调用一次
// 添加水印的方法
watermark.set = (str, font) => {
  let id = setWatermark(str, font)
  _interval = setInterval(() => {
    if (document.getElementById(id) === null) {
      id = setWatermark(str, font)
    }
  }, 500)
  window.onresize = () => {
    setWatermark(str, font)
  }
}

watermark.setById = (str, font, id, parentId) => {
  setWatermarkById(str, font, id, parentId)
  window.onresize = () => {
    const parentWidth = document.getElementById(id).clientWidth
    const newFont = parentWidth > 1000 ? '48px STSong' : '12px STSong'
    // console.log('resize了', parentWidth, newFont)
    setWatermarkById(str, newFont, id, parentId)
  }
}

// 移除水印的方法
watermark.remove = () => {
  if (document.getElementById(idd) !== null) {
    var box = document.getElementById(idd)
    box.parentNode.removeChild(box)
    _interval ? clearInterval(_interval) : ''
  }
  window.onresize = null
}

// 根据id移除水印的方法
watermark.removeById = (id) => {
  if (document.getElementById(id) !== null) {
    var box = document.getElementById(id)
    box.parentNode.removeChild(box)
  }
  window.onresize = null
}

watermark.setWatermarkById = setWatermarkById

export default watermark
