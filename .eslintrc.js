module.exports = {
  env: {
    browser: true,
    es2021: true,
    node: true,
  },
  extends: 'airbnb-base',
  overrides: [
    {
      env: {
        node: true,
      },
      files: [
        '.eslintrc.{js,cjs}',
      ],
      parserOptions: {
        sourceType: 'script',
      },
    },
  ],
  parserOptions: {
    ecmaVersion: 'latest',
    sourceType: 'module',
  },
  rules: {
  },
  settings: {
    'import/resolver': {
      node: {
        paths: ['/Users/seongmingyu/AndroidStudioProjects/Selection'], // 프로젝트의 실제 경로에 맞게 조정
      },
    },
  },
};
