<template>
  <div>
    <Row type="flex" justify="center" align="top" class="code-row-bg" :style="{maxWidth:'1800px',margin: '0 auto'}">
      <i-col :xs="{ span: 22, offset: 1 }" :sm="{span: 22, offset: 1}" :md="{span: 15, offset: 1}"
             :lg="{span: 14}" :style="{marginRight:'30px'}">
        <el-card class="box-card card-border" :style="{padding: '30px',marginTop: '20px', textAlign: 'center'}">
          <vue-qr id="qrcode" :bgSrc='config.imagePath' :logoSrc="config.imagePath" :text="config.url" height="300" width="300"></vue-qr>
        </el-card>
        <el-card class="box-card card-border" :style="{padding: '30px',marginTop: '20px', textAlign: 'center'}">
          <img id="mydrawpic" :src="config.imgpath"/>
        </el-card>
      </i-col>
      <i-col :xs="{ span: 22}" :sm="{span: 22}" :md="{span: 6}"
             :lg="{span: 5}">
      </i-col>
    </Row>
  </div>
</template>

<script>
  import request from '@/components/request'
  import VueQr from 'vue-qr'
  import vc from 'vue-cookie'

  export default {
    data () {
      return {
        config: {
          imagePath: 'https://avatars2.githubusercontent.com/u/33079130?s=460&v=4',
          url: '',
          imgpath: 'https://avatars2.githubusercontent.com/u/33079130?s=460&v=4'
        },
        identity: '',
        ws: null
      }
    },
    created () {
      this.generalurl()
      this.init()
    },
    mounted () {
    },
    methods: {
      generalurl : function() {
        if (vc.get('token-qrcode')) {
          this.identity  = vc.get('token-qrcode')
        } else {
          this.identity = 'qrcode'+this.initidentity(10);
          vc.set('token-qrcode', this.identity, 1);
        }
        let tempurl='http://draw.niezhiliang.com/phone?params='+this.identity
        this.$set(this.config,'url',tempurl)

      },
      init : function() {
        if (vc.get('token-qrcode')) {
          this.identity  = vc.get('token-qrcode')
        } else {
          this.identity = 'qrcode'+this.initidentity(10);
          vc.set('token-qrcode', this.identity, 1);
        }
        let url = "ws://draw.niezhiliang.com:8080/socketServer/"+this.identity
        if ('WebSocket' in window) {
          this.ws = new WebSocket(url)
        } else if('MozWebSocket' in window) {
          this.ws = new MozWebSocket(url)
        }
        //普通的js写法不支持赋值操作
        this.ws.onmessage = (evt) => {
          console.log(evt.data)
          this.config.imgpath = evt.data
        }
      },
      initidentity : function(size) {
        var seed = new Array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','P','Q','R','S','T','U','V','W','X','Y','Z',
          'a','b','c','d','e','f','g','h','i','j','k','m','n','p','Q','r','s','t','u','v','w','x','y','z',
          '2','3','4','5','6','7','8','9'
        );
        //数组
        var seedlength = seed.length;//数组长度
        var createPassword = '';
        var i; var j = '' ;
        for (i = 0; i < size; i++) {
          j = Math.floor(Math.random() * seedlength);
          createPassword += seed[j];
        }
        return createPassword;
      }
    },
    components: { VueQr }
  }
</script>
