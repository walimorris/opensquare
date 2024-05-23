const path = require('path');
const os = require('os');

const platformPath = os.platform().includes('Windows') ? '.frontend/app.js' : 'frontend/app.js';

module.exports = {
    entry: path.resolve(__dirname, platformPath),
    devtool: 'inline-source-map',
    cache: true,
    mode: 'development',
    output: {
        path: __dirname,
        filename: 'frontend/static/built/bundle.js'
    },
    performance: {
        hints: false,
        maxEntrypointSize: 512000,
        maxAssetSize: 512000
    },
    module: {
        rules: [
            {
                test: /\.(js)$/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }]
            },
            {
                test: /\.(scss|css)$/,
                use: ['style-loader', 'css-loader']
            },
        ]
    }
};
