window.$config = {
    local: {
        VUE_APP_BASE_API: 'http://localhost:9998/tempRegister',
        MEDIA_API: ''
    },
    production: {
        VUE_APP_BASE_API: '/tempRegister'
    }
}