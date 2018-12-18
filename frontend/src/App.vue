<template>
  <div id="app">
    <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>
    <router-view/>
  </div>
</template>

<script>
import vc from 'vue-cookie'

export default {
  name: 'app',
  created() {
    this.init()
  },
  methods: {
    init : function() {
      let ws = null;
      let identity = '';
      if (vc.get('token')) {
        identity  = vc.get('token')
      } else {
        identity = this.initidentity(10);
        vc.set('token', identity, 1);
      }
      let url = "ws://127.0.0.1:8080/socketServer/"+identity
      console.log(url)
      if ('WebSocket' in window){
        ws = new WebSocket(url);
      }
      else if ('MozWebSocket' in window){
        ws = new MozWebSocket(url);
      }
    },
    initidentity : function(size) {
      var seed = new Array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','P','Q','R','S','T','U','V','W','X','Y','Z',
        'a','b','c','d','e','f','g','h','i','j','k','m','n','p','Q','r','s','t','u','v','w','x','y','z',
        '2','3','4','5','6','7','8','9'
      );//数组
      var seedlength = seed.length;//数组长度
      var createPassword = '';
      var i; var j = '';
      for (i=0;i<size;i++) {
        j = Math.floor(Math.random()*seedlength);
        createPassword += seed[j];
      }
      return createPassword;
    }
  }
}
</script>

<style>
  body{
    margin: 0px;
    background-color: #f4f4f4;
  }
</style>
