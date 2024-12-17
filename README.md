[![](https://jitpack.io/v/ibrahim4Hamdy/product-options.svg)](https://jitpack.io/#ibrahim4Hamdy/product-options)

# Product Options Library

**Product Options** is a flexible Android library that provides three customizable UI components:

- **Image Picker**
- **Color Picker**
- **Size Picker**

This library simplifies adding product options such as colors, sizes, and images with smooth user interaction.

---

## 📦 Installation

To use the library, add the **JitPack** repository and the dependency to your project.

### 1. Add JitPack to `settings.gradle`:

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### 2. Add the dependency to `build.gradle` (Module):

```groovy
dependencies {
    implementation 'com.github.ibrahim4Hamdy:product-options:Tag'
}
```
> Replace `Tag` with the latest version of the library.

---

## 🚀 Usage

### 1. **Image Picker**

The Image Picker component allows displaying and selecting images.

#### Java Code:

```java
ImagePickerView imagePickerView = findViewById(R.id.imagePickerView);
List<String> images = Arrays.asList(
        "shoes_1", // Resource images
        "shoes_2",
        "shoes_3"
);
imagePickerView.setImages(images);
imagePickerView.setImageSelectedListener(selectedImageIndex -> {
    Toast.makeText(this, "Selected image index: " + selectedImageIndex, Toast.LENGTH_SHORT).show();
});
```

#### XML Layout:

```xml
<com.bemo.product.option.ImagePickerView
    android:id="@+id/imagePickerView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:layout_margin="16dp" />
```

---

### 2. **Color Picker**

The Color Picker component allows users to select a color from a given list.

#### Java Code:

```java
ColorPickerView colorPickerView = findViewById(R.id.colorPickerView);
int[] colors = {
        Color.BLUE,
        Color.LTGRAY,
        Color.DKGRAY
};
colorPickerView.setColors(colors);
colorPickerView.setOnColorSelectedListener((selectedColorIndex, color) -> {
    Toast.makeText(MainActivity.this, "Selected color: " + color, Toast.LENGTH_SHORT).show();
});
```

#### XML Layout:

```xml
<com.bemo.product.option.ColorPickerView
    android:id="@+id/colorPickerView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:background="#F8F8F8"
    app:indicatorColor="#FF0000"
    app:scrollbarVisibility="false"
    app:radius="13dp"
    android:padding="2dp"
    app:defaultSelectedIndex="1"
    app:circleSize="8dp"
    app:indicatorSize="12dp" />
```

---

### 3. **Size Picker**

The Size Picker component allows users to select product sizes such as `S`, `M`, `L`, etc.

#### Java Code:

```java
SizePickerView sizePickerView = findViewById(R.id.sizePicker);
sizePickerView.setSizes(new String[]{"S", "M", "L", "XL", "2XL"});
sizePickerView.setOnSizeSelectedListener(size -> {
    Toast.makeText(this, "Selected size: " + size, Toast.LENGTH_SHORT).show();
});
```

#### XML Layout:

```xml
<com.bemo.product.option.SizePickerView
    android:id="@+id/sizePicker"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:selectedTextColor="#FFF"
    app:defaultTextColor="#000"
    app:indicatorColor="#FF6F00"
    app:defaultSelectedIndex="2"
    app:textSize="5sp"
    app:boxRadius="10"
    app:indicatorPadding="8dp" />
```

---

## 🛠️ Customization

The library provides various attributes to customize the behavior and appearance of the components:

### **Color Picker Attributes**:
| Attribute               | Description                          | Example Value  |
|-------------------------|--------------------------------------|----------------|
| `app:indicatorColor`    | Sets the indicator color             | `#FF0000`      |
| `app:radius`            | Sets circle radius                  | `13dp`         |
| `app:circleSize`        | Sets circle size                    | `8dp`          |
| `app:indicatorSize`     | Sets the indicator size             | `12dp`         |
| `app:defaultSelectedIndex` | Sets the default selected color index | `1`            |

### **Size Picker Attributes**:
| Attribute               | Description                          | Example Value  |
|-------------------------|--------------------------------------|----------------|
| `app:selectedTextColor` | Sets the color of selected text      | `#FFF`         |
| `app:defaultTextColor`  | Sets the color of unselected text    | `#000`         |
| `app:indicatorColor`    | Sets the indicator color             | `#FF6F00`      |
| `app:textSize`          | Sets the text size                  | `5sp`          |
| `app:boxRadius`         | Sets the corner radius of boxes      | `10`           |
| `app:indicatorPadding`  | Sets padding around the indicator    | `8dp`          |

---

## 📄 License

This library is available under the **MIT License**. See the LICENSE file for details.

---

## 🌟 Contributing

Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Submit a pull request.

For major changes, please open an issue first to discuss what you would like to change.

---

## 🔗 Links

- **Project URL:** [https://github.com/ibrahim4Hamdy/product-options](https://github.com/ibrahim4Hamdy/product-options)
- **Latest Release:** [Releases](https://github.com/ibrahim4Hamdy/product-options/releases)

---

Happy coding! 🚀
