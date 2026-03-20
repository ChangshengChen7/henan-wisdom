# 智慧河南 App v2.0 - 完整项目规划

## 项目目标
打造一个应用商店级别的精美学习App，UI精美、功能齐全、交互流畅。

---

## 一、功能规划

### 1. 首页 (HomeScreen)
**功能：**
- 今日学习概览卡片
  - 已学习卡片数
  - 待复习卡片数
  - 学习时长
- 快速开始学习按钮
- 分类选择（历史/地理/文化/经济）
- 每日学习目标进度条

**UI设计：**
- 顶部：用户头像 + 问候语
- 中间：统计卡片（圆角、阴影、渐变背景）
- 底部：分类标签（横向滚动）

### 2. 学习页 (StudyScreen)
**功能：**
- 知识卡片翻转动画
- 显示问题和答案
- 评分按钮（太难/一般/简单）
- FSRS间隔重复算法
- 进度指示器
- 完成庆祝动画

**UI设计：**
- 卡片：圆角、阴影、翻转3D效果
- 评分按钮：彩色图标、点击反馈
- 进度条：渐变色

### 3. 进度页 (ProgressScreen)
**功能：**
- 学习统计图表
  - 本周学习曲线
  - 各分类掌握度饼图
- 日历热力图（学习打卡）
- 成就徽章

**UI设计：**
- 图表：Material Design风格
- 热力图：日历格子+颜色深浅
- 徽章：精美图标

### 4. 设置页 (SettingsScreen)
**功能：**
- 每日学习目标设置
- 学习提醒时间
- 深色/浅色主题切换
- 关于页面

**UI设计：**
- 列表项：图标+标题+开关/箭头
- 分组卡片

---

## 二、UI设计规范

### 颜色主题（河南文化）
- 主色：青铜绿 #4A7C59
- 强调色：黄河金 #D4AF37
- 辅助色：甲骨文橙 #E8A87C
- 错误色：牡丹红 #B85C5C

### 字体
- 标题：Noto Sans SC Bold
- 正文：Noto Sans SC Regular

### 圆角
- 卡片：16dp
- 按钮：8dp
- 输入框：4dp

### 阴影
- 卡片：elevation 4dp
- 按钮：elevation 2dp

---

## 三、技术架构

### 版本兼容性（已验证）
| 组件 | 版本 | 说明 |
|------|------|------|
| Kotlin | 1.9.22 | 与AGP 8.2.0兼容 |
| AGP | 8.2.0 | 与Gradle 8.5兼容 |
| Gradle | 8.5 | 使用wrapper |
| Compose BOM | 2024.02.00 | 与Kotlin 1.9.22兼容 |
| Hilt | 2.50 | 最新稳定版 |
| Room | 2.6.1 | 最新稳定版 |

### 架构模式
- Clean Architecture (UI/Domain/Data)
- MVVM + StateFlow
- Repository Pattern

### 依赖
```kotlin
// Compose
implementation(platform("androidx.compose:compose-bom:2024.02.00"))
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.ui:ui-tooling-preview")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.6")

// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")

// Hilt
implementation("com.google.dagger:hilt-android:2.50")
implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

// DataStore
implementation("androidx.datastore:datastore-preferences:1.0.0")

// Charts
implementation("com.patrykandpatrick.vico:compose:1.13.1")
```

---

## 四、文件结构

```
app/src/main/java/com/henan/wisdom/
├── MainActivity.kt
├── HenanWisdomApp.kt
├── navigation/
│   └── NavGraph.kt
├── ui/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   ├── components/
│   │   ├── FlipCard.kt
│   │   ├── ProgressCard.kt
│   │   ├── StatCard.kt
│   │   └── RatingButtons.kt
│   └── screens/
│       ├── HomeScreen.kt
│       ├── StudyScreen.kt
│       ├── ProgressScreen.kt
│       └── SettingsScreen.kt
├── domain/
│   ├── model/
│   │   ├── KnowledgeCard.kt
│   │   ├── StudyStats.kt
│   │   └── CardRating.kt
│   ├── repository/
│   │   ├── CardRepository.kt
│   │   └── ProgressRepository.kt
│   └── usecase/
│       ├── GetCardsUseCase.kt
│       ├── SaveRatingUseCase.kt
│       └── GetStudyStatsUseCase.kt
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt
│   │   ├── CardDao.kt
│   │   ├── CardEntity.kt
│   │   └── ProgressDao.kt
│   └── repository/
│       ├── CardRepositoryImpl.kt
│       └── ProgressRepositoryImpl.kt
└── di/
    └── AppModule.kt
```

---

## 五、实现步骤

### Phase 1: 基础架构 ✅
- [x] 项目配置
- [x] Domain Layer
- [x] Data Layer
- [x] 主题配置

### Phase 2: UI组件
- [ ] FlipCard（翻转卡片）
- [ ] StatCard（统计卡片）
- [ ] RatingButtons（评分按钮）
- [ ] ProgressCard（进度卡片）

### Phase 3: 页面实现
- [ ] HomeScreen（首页）
- [ ] StudyScreen（学习页）
- [ ] ProgressScreen（进度页）
- [ ] SettingsScreen（设置页）

### Phase 4: 功能实现
- [ ] 卡片学习流程
- [ ] FSRS算法
- [ ] 数据持久化
- [ ] 统计图表

### Phase 5: 打磨优化
- [ ] 动画效果
- [ ] 深色模式
- [ ] 性能优化
- [ ] 测试

---

## 六、验收标准

1. **UI精美度**
   - 所有页面有完整设计，无占位符
   - 颜色、字体、圆角统一
   - 动画流畅自然

2. **功能完整度**
   - 学习功能可用
   - 统计数据真实
   - 设置功能有效

3. **用户体验**
   - 操作流畅
   - 反馈及时
   - 无明显bug

4. **应用商店标准**
   - 启动正常
   - 无崩溃
   - 内存合理