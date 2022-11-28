const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: [
    'vuetify'
  ],
  lintOnSave: false,
  configureWebpack: {
		module: {
			rules: [
			  {
				test: /\.geojson$/,
				loader: 'json-loader'
			  }
			]
		  }
	}
})
