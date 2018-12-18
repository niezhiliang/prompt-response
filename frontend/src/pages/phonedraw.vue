<template>
  <div class="inner draw" @mousemove="beginPath($event)">
    <div class="wrap" style="position: relative;">
      <canvas
        id="canvas"
        class="fl"
        :width="screenWidth"
        :height="screenHeight"
        @mousedown="canvasDown($event)"
        @mouseup="canvasUp($event)"
        @mousemove="canvasMove($event)"
        @touchstart="canvasDown($event)"
        @touchend="canvasUp($event)"
        @touchmove="canvasMove($event)"
      >
      </canvas>
      <div id="control" style="float: left">
        <div id="canvas-control">
          <button class="el-button btn-orange el-button--primary"
                  @click="controlCanvas('clear')">
            清除
          </button>
          <button @click="getImage" class="el-button btn-orange el-button--primary">
            确定
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style >
  @media screen and (max-width: 9000px) {
    #img-box,
    #canvas-drawImage h5,
    #canvas-brush {
      display: none;
    }
    #canvas-drawImage button {
      width: auto;
      position: absolute;
      flex: 1;
    }
    .wrap #control {
      height: 40px;
      display: flex;
      flex-direction: row;
      text-align: center;
      position: absolute;
      bottom: 35%;
      transform: rotate(10deg);
        -ms-transform: rotate(90deg); /* IE 9 */
        -moz-transform: rotate(90deg); /* Firefox */
        -webkit-transform: rotate(90deg); /* Safari 和 Chrome */
        -o-transform: rotate(90deg); /* Opera */
    }
  }
  .wrap {
    border: 1px #585858 solid;
    overflow: hidden;
    margin: 0 auto;
  }

  #canvas-control button {
    width: 100px;
    height: 60px;
    font-size: 33px;
    border-radius: 10px;
  }

  #control div {
    margin-top: 60px;
    margin-bottom: 60px;
  }
</style>
<script>
  import request from '@/components/request'
  import vc from 'vue-cookie'

  export default {
    data () {
      return {
        context: {},
        imgUrl: [],
        canvasMoveUse: true,
        // 存储当前表面状态数组-上一步
        preDrawAry: [],
        // 存储当前表面状态数组-下一步
        nextDrawAry: [],
        // 中间数组
        middleAry: [],
        // 配置参数
        config: {
          lineWidth: 3,
          lineColor: '#393939',
          shadowBlur: 1
        },
        screenWidth: null,
        screenHeight: null
      }
    },
    created () {
      this.screenWidth = screen.availWidth
      this.screenHeight = screen.availHeight
    },
    mounted () {
      const canvas = document.querySelector('#canvas')
      this.context = canvas.getContext('2d')
      this.initDraw()
      this.setCanvasStyle()
    },
    watch: {
      screenWidth (val) {
        this.screenWidth = val
      }
    },
    methods: {
      isPc () {
        const userAgentInfo = navigator.userAgent
        const Agents = ['Android', 'iPhone', 'SymbianOS', 'Windows Phone', 'iPad', 'iPod']
        let flag = true
        for (let v = 0; v < Agents.length; v++) {
          if (userAgentInfo.indexOf(Agents[v]) > 0) {
            flag = false
            break
          }
        }
        return flag
      },
      removeImg (src) {
        this.imgUrl = this.imgUrl.filter(item => item !== src)
      },
      initDraw () {
        const preData = this.context.getImageData(0, 0, 600, 400)
        // 空绘图表面进栈
        this.middleAry.push(preData)
      },
      canvasMove (e) {
        if (this.canvasMoveUse) {
          console.log('canvasMove')
          const t = e.target
          let canvasX
          let canvasY
          if (this.isPc()) {
            canvasX = e.clientX - t.parentNode.offsetLeft
            canvasY = e.clientY - t.parentNode.offsetTop
          } else {
            canvasX = e.changedTouches[0].clientX - t.parentNode.offsetLeft
            canvasY = e.changedTouches[0].clientY - t.parentNode.offsetTop
          }
          this.context.lineTo(canvasX, canvasY)
          this.context.stroke()
        }
      },
      beginPath (e) {
        const canvas = document.querySelector('#canvas')
        if (e.target !== canvas) {
          console.log('beginPath')
          this.context.beginPath()
        }
      },
      // mouseup
      canvasUp (e) {
        console.log('canvasUp')
        const preData = this.context.getImageData(0, 0, 600, 400)
        if (!this.nextDrawAry.length) {
          // 当前绘图表面进栈
          this.middleAry.push(preData)
        } else {
          this.middleAry = []
          this.middleAry = this.middleAry.concat(this.preDrawAry)
          this.middleAry.push(preData)
          this.nextDrawAry = []
        }
        this.canvasMoveUse = false
      },
      // mousedown
      canvasDown (e) {
        console.log('canvasDown')
        this.canvasMoveUse = true
        // client是基于整个页面的坐标
        // offset是cavas距离顶部以及左边的距离
        const canvasX = e.clientX - e.target.parentNode.offsetLeft
        const canvasY = e.clientY - e.target.parentNode.offsetTop
        this.setCanvasStyle()
        // 清除子路径
        this.context.beginPath()
        this.context.moveTo(canvasX, canvasY)
        console.log('moveTo', canvasX, canvasY)
        // 当前绘图表面状态
        const preData = this.context.getImageData(0, 0, 600, 400)
        // 当前绘图表面进栈
        this.preDrawAry.push(preData)
        e.preventDefault()
      },
//      // 设置颜色
//      setColor(color) {
//        this.config.lineColor = color
//      },
      // 设置笔刷大小
      setBrush(type) {
        this.config.lineWidth = type
      },
      // 操作
      controlCanvas (action) {
        switch (action) {
          case 'clear':
            this.context.clearRect(0, 0, this.context.canvas.width, this.context.canvas.height)
            this.preDrawAry = []
            this.nextDrawAry = []
            this.middleAry = [this.middleAry[0]]
            break
        }
      },
      // 生成图片
      getImage () {
        const canvas = document.querySelector('#canvas')
        const src = canvas.toDataURL('image/png')
        let pic = src;
        let id = null
        let url = window.location.href;
        id = url.substring(url.lastIndexOf('=') + 1)
        console.log(id)
        request.post('upload', { base64: pic, userid: id }).then(res => {
          if (res.data.status === 20) {
          alert('绘制成功,请到pc端查看')
          window.opener = null
          window.close()
        }
      }).catch(function (err) {
          alert(err)
        })
      },
      // 设置绘画配置
      setCanvasStyle () {
        this.context.lineWidth = this.config.lineWidth
        // this.context.shadowBlur = this.config.shadowBlur
        // this.context.shadowColor = this.config.lineColor
        // this.context.strokeStyle = this.config.lineColor
      },
      getViewportSize () {
        return {
          width: window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth,
          height: window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight
        }
      }
    }
  }
</script>
