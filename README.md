# 🧱 JavaFX Breakout: Evolution

<p align="center">
  <img src="https://img.shields.io/badge/Durum-Geliştirme_Aşamasında-green?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Versiyon-0.1.0-blue?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Dil-Java-orange?style=for-the-badge&logo=java" />
</p>

---

## 🎮 Proje Hakkında
Bu projeyi, oyun motorlarının kaputunun altındaki **Matematik**, **Fizik** ve **Geometri** kurallarını anlayarak **Oyun Geliştirme** mantığını temelden kavramak amacıyla geliştiriyorum. Hazır fizik motorları kullanmak yerine, çarpışma algoritmalarını (Collision Detection) ve vektörel hareketleri sıfırdan inşa ediyorum.

---

## ⚡ Planlanan Mekanikler ve Evrim
| Özellik | Açıklama | Durum |
| :--- | :--- | :--- |
| **Durum Yönetimi** | Ana Menü ve Oyun Bitti (Game Over) ekranları | Yükleniyor... |
| **Can Sistemi** | Hayatta kalmak için 3 Kalp (Can) hakkı | Yükleniyor... |
| **Seviye Sistemi** | Seviye bazlı, kademeli zorluk artışı | Planlandı |
| **Özelleştirme** | Top ve Raket için renk seçenekleri (3. Seviye Sonrası) | Kilitli |
| **Fizik Motoru** | AABB ve çember çarpışma (Collision) algoritmaları | **Aktif** |

---

## 🛠️ Teknik Mimari ve Oyun Fiziği

### 1. Ekran Koordinat Sistemi (Ters Y-Ekseni)
Standart Kartezyen sistemin aksine, bilgisayar grafiklerinde `(0,0)` noktası sol üst köşedir.
- Topu aşağı indirmek için Y değeri **artırılır** (`ballDY = +3;`).
- Topu yukarı çıkarmak için Y değeri **azaltılır** (`ballDY = -3;`).

### 2. Duvar ve Çember Çarpışmaları (Bounding Radius)
Merkez noktasını değil, topun çeperini (kenarlarını) takip ediyoruz.
Sol duvar çarpışması: `if (ballX - BALL_R < 0)`
Çarpışma anında vektör yansıması (Elastic Collision) sağlamak için hızı -1 ile çarparak yönü tam tersine çeviriyoruz (`ballDX *= -1`).

### 3. "Penetration Resolution" (İçe Geçmeyi Engelleme)
60 FPS döngüsünde, top hızla hareket ederken panelin (paddle) içine birkaç piksel gömülebilir. Bunu önlemek ve topun titremesini (Tunneling Effect) durdurmak için çarpışma anında topu panelin tam yüzeyine ışınlıyoruz:
`ballY = PADDLE_Y - BALL_R;`

### 4. AABB-Daire Çarpışma Algoritması (Tuğlalar İçin)
Tuğlaların (Brick) topla çarpışmasını hesaplamak için "Axis-Aligned Bounding Box" ve Çember kesişim mantığı kullandım. Performans optimizasyonu için Pisagor teoremindeki **Karekök (`Math.sqrt()`) alma işleminden kaçınıldı**. Bunun yerine mesafelerin kareleri karşılaştırıldı ("Avoid the square root" tekniği).

---

## 🏗️ Oyun Mimarisi (UML Tasarımı)

Aşağıda ana oyun motorumuz (`GamePane`) ve Tuğla varlıklarının (`Brick`) arasındaki Nesne Yönelimli Programlama (OOP) yapısı bulunmaktadır.
```mermaid
classDiagram
    class GamePane {
        ~ int W
        ~ int H
        ~ double BALL_R
        ~ double PADDLE_W
        - double ballX
        - double ballY
        - double ballDX
        - double ballDY
        - double paddleX
        - Brick[][] bricks
        + start(stage: Stage) void
        + handle(now: long) void
        ~ update() void
        ~ draw(gc: GraphicsContext) void
        ~ initBricks() void
        ~ checkBrickCollision() void
    }
    
    class Brick {
        - double x
        - double y
        - double width
        - double height
        - boolean alive
        - Color color
        + draw(gc: GraphicsContext) void
        + intersects(ballX: double, ballY: double, ballR: double) boolean
        + destroy() void
        + isAlive() boolean
    }
    
    GamePane "1" *-- "many" Brick : kullanır (Composition)
