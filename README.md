# 🎯 StepsProgressBar

A sleek, animated, floating-point-compatible step-based progress bar for Android.  
Now with easy setup using a single config object! 💡

---

## 🖼️ Preview

<!-- Add your gif/image URL after uploading -->
<img src="https://github.com/aahanverma00710/StepProgressBarView/blob/main/art/1.png" alt="FloatStepBar Demo" width="500"/>

---

## 🚀 Features

- ✅ **Floating Point Support**: Progress like `2.5 / 5`
- 🔧 **Simple Configuration**: Use `ProgressViewConfig` to apply all customizations
- 🎨 **Customizable Appearance**:
  - Bar height & rounded corners
  - Filled & unfilled bar colors
  - Custom thumb drawable
  - Step label suffix, size, font, and color
- 🌀 **Smooth Animations** on step change
- 🔄 **Programmatic Jump** with `jumpToStep(step, animated)`
- 🪝 **Step Change Callback** with `setOnStepChangeListener`

---

## 🛠 Usage

### Step 1: Add the view in your layout

```xml
<com.avcoding.discreetprogress.StepProgressBarView
    android:id="@+id/stepProgressBar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

### Step 2: Set up in Kotlin
Use the new ProgressViewConfig to configure the view easily:

``` kotlin
val config = ProgressViewConfig(
    totalSteps = 5,
    labelSuffix = "m",
    labelTextSizeSp = 14f,
    labelTextColor = Color.DKGRAY,
    barHeightDp = 10,
    roundRadius = 6,
    thumbDrawable = ContextCompat.getDrawable(this, R.drawable.ic_thumb),
    barColor = Pair(Color.BLUE, Color.LTGRAY)
)

stepProgressBar.setUp(config) { step ->
    Log.d("StepBar", "Current progress: $step")
}

// Optionally jump to a value
stepProgressBar.jumpToStep(3.5f, animated = true)
```


📦 Installation
### Option 1: Manual (module level)
#### Clone or download this repo.

Copy the StepProgressBarView.kt and ProgressViewConfig.kt into your project

```
// settings.gradle
dependencyResolutionManagement {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

// build.gradle
dependencies {
     implementation 'com.github.aahanverma00710:StepProgressBarView:1.0'
}
```
🧑‍💻 Author
### Made with ❤️ by Aahan Verma
