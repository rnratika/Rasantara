# 🍲 Rasantara - Buku Resep Makanan Berbagai Negara

**Rasantara** adalah aplikasi Android native yang memungkinkan pengguna untuk menjelajahi berbagai resep masakan dari seluruh dunia. Aplikasi ini dirancang dengan antarmuka pengguna yang modern dan interaktif, dilengkapi dengan fitur pencarian, filter berdasarkan negara, dan kemampuan menyimpan resep favorit yang dapat diakses meskipun tanpa koneksi internet (Offline).

## ✨ Fitur Utama

* **Welcome Screen (Launcher):** Halaman onboarding interaktif yang menyambut pengguna saat pertama kali membuka aplikasi.
* **Eksplorasi Resep Global:** Jelajahi resep makanan dari berbagai negara (Inggris, Italia, Jepang, Meksiko, dll) melalui integrasi REST API.
* **Mode Offline (Resep Favorit):** Simpan resep favorit ke dalam database lokal. Resep yang telah difavoritkan tetap dapat dilihat meskipun perangkat sedang tidak terhubung ke jaringan internet.
* **Penanganan Error & Jaringan:** Dilengkapi dengan tampilan khusus (UI kabel putus) ketika gagal mengambil data dari API, lengkap dengan tombol **Muat Ulang (Refresh)**.
* **Dukungan Tema Dinamis:** Aplikasi sepenuhnya mendukung **Light Theme** dan **Dark Theme** sesuai dengan pengaturan sistem perangkat pengguna.

## 🛠️ Spesifikasi Teknis & Teknologi yang Digunakan

Aplikasi ini dibangun dengan mematuhi standar pengembangan Android modern, mengimplementasikan:

* **Bahasa Pemrograman:** Kotlin
* **Arsitektur & Navigasi:**
* Minimal 2 Activity (`WelcomeActivity` sebagai Launcher, `MainActivity`, dan `RecipeDetailActivity`).
* Berpindah halaman dan mengirim data menggunakan **Intent**.
* Pengelolaan fragment (`Home`, `Explore`, `Favorite`, `Profile`) menggunakan **Jetpack Navigation Component** (`nav_graph.xml`) dan *Bottom Navigation View*.


* **User Interface (UI):** XML Layouts, Material Components, dan **RecyclerView** beserta *Custom Adapter* (`RecipeAdapter`, `FavoriteAdapter`) untuk menampilkan daftar resep.
* **Networking (API):** **Retrofit2** & Gson Converter (`ApiConfig`, `ApiService`) untuk mengambil data resep secara *remote*.
* **Local Persistence:** **Room Database (SQLite)** (`RecipeDatabase`, `FavoriteDao`) untuk menyimpan data resep favorit secara lokal.
* **Background Threading:** Memanfaatkan **Executor Service** (`java.util.concurrent.Executor`) untuk menjalankan operasi berat seperti proses baca/tulis database agar tidak memblokir Main/UI Thread.

## 📂 Struktur Proyek

Proyek ini menggunakan struktur package berdasarkan fitur (*package by feature*) yang rapi dan mudah di-*maintain*:

```text
com.example.rasantara
│
├── data/
│   ├── local/          # Konfigurasi Room Database, Entity (FavoriteRecipe), dan DAO
│   ├── model/          # Data class / POJO untuk response API (RecipeResponse.kt)
│   └── remote/         # Konfigurasi Retrofit (ApiConfig) dan Endpoint (ApiService)
│
├── ui/
│   ├── adapter/        # RecyclerView Adapters (RecipeAdapter, FavoriteAdapter)
│   ├── explore/        # ExploreFragment untuk mencari dan filter resep
│   ├── favorite/       # FavoriteFragment untuk menampilkan resep tersimpan (Offline)
│   ├── main/           # HomeFragment (Beranda utama aplikasi)
│   └── profile/        # ProfileFragment
│
├── MainActivity        # Host untuk Navigation Component (Fragment)
├── WelcomeActivity     # Launcher Activity / Splash Screen
└── RecipeDetailActivity# Activity untuk menampilkan detail lengkap masakan

```

## 🚀 Cara Menjalankan Aplikasi (Instalasi)

1. Clone repositori ini:
```
git clone https://github.com/rnratika/rasantara.git
```
2.  Buka **Android Studio** (Disarankan versi terbaru seperti *Iguana* atau *Jellyfish*).
3.  Pilih **File > Open**, lalu navigasikan ke folder repositori yang baru saja di-*clone*.
4.  Tunggu hingga proses *Gradle Sync* selesai sepenuhnya.
5.  Siapkan Emulator Android atau hubungkan perangkat fisik (Pastikan USB Debugging aktif).
6.  Tekan tombol **Run (Shift + F10)**.
