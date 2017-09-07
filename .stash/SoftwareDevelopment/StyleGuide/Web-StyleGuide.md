# Web StyleGuide

# Syntax

## HTML

## DOM

## CSS

# React Component

## 数据独立于逻辑




# Project Architecture: 项目架构

## 分层独立可测试的架构

## 抽象出公共独立代码

## 按特性划分结构
```
// maybe bad
.
├── components
│   ├── todos
│   └── user
├── reducers
│   ├── todos
│   └── user
└── tests
    ├── todos
    └── user

// maybe good
.
├── todos
│   ├── component
│   ├── reducer
│   └── test
└── user
    ├── component
    ├── reducer
    └── test
```

## 渐进式持续重构