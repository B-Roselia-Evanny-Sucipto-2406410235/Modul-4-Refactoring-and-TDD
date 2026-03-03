Link: https://eshop-roselia.koyeb.app/

## Modul 2
Selama pengerjaan modul, saya memperbaiki beberapa masalah kualitas kode yang terdeteksi oleh SonarCloud. Isu pertama adalah field injection. SonarCloud menemukan bahwa ProductController menggunakan @Autowired langsung pada field. Maka, saya melakukan refactoring menjadi constructor injection dengan mendeklarasikan service sebagai `private final` dan membuat konstruktor publik. Hal ini meningkatkan kemudahan testing dan memastikan class tidak pernah dalam keadaan tidak utuh saat di-instantiate. Kemudian, saya juga memperbaiki dependensi yang tidak terorganisir pada file `build.gradle.kts`. Saya memperbaikinya dengan mengelompokkan dependensi berdasarkan tujuannya, seperti dependensi aplikasi, testing, dll. Hal ini meningkatkan maintainability script build dan memudahkannya untuk dibaca.

Lalu, menurut saya, implementasi saat ini sudah memenuhi definisi continuous integration dan continuous deployment. Untuk bagian CI, setiap kali saya melakukan push atau pull kode ke branch main, GitHub Actions akan otomatis menjalankan pengujian dan analisis kualitas kode melalui SonarCloud dan Scorecard. SonarCloud dan Scorecard akan memastikan hanya kode berkualitas tinggi yang diintegrasikan. Untuk bagian CD, integrasi otomatis dengan Koyeb memastikan bahwa setiap perubahan yang lolos pengecekan CI akan langsung dibuild menjadi Docker image. Kemudian, dideploy ke lingkungan production secara otomatis. Workflow ini meminimalkan risiko kesalahan dan menjamin versi terbaru aplikasi selalu tersedia dalam status healthy.

## Modul 3
1. Prinsip yang diterapkan pada proyek
Pada proyek ini, telah diterapkan prinsip SOLID untuk memastikan kode mudah di-maintain dan di-testing.
- Single Responsibility Principle (SRP):
Saya memisahkan CarController dari ProductController. Sebelumnya, CarController berada di file yang sama dengan ProductController dan melakukan extends pada ProductController. Sekarang, masing-masing controller sudah dipisah dan memiliki tugas masing-masing. 
- Open-Closed Principle (OCP)
Saya membuat BaseRepository yang berisfat generik. Hal ini memungkinkan kode untuk terbuka terhadap penambahan/extension tetapi tertutup untuk modifikasi, sehingga tidak perlu mengubah kode setiap ada entitas baru.
- Liskov Substitution Principle (LSP)
Saya menghapus hubungan inheritance antara CarController dengan ProductController, karena CarController tidak dapat menggantikan ProductController.
- Interface Segregation Principle (ISP)
Saya tetap memisahkan interface CarService dan ProductService untuk memastikan bahwa jika CarService membutuhkan metode spesifik baru, ProductService tidak dipaksa untuk mengimplementasikannya.
- Dependency Inversion Principle (DIP)
Saya mengubah controller (CarController) dan service (CarServiceImpl dan ProductServiceImpl) agar bergantung pada interface (sebelumnya bergantung pada kelas konkret).

2. Keuntungan menerapkan prinsip SOLID
- Dengan menerapkan prinsip SOLID, aplikasi yang dibuat lebih mudah di-maintain dan ditambahkan fitur. Dengan menerapkan OCP, jika kebutuhan bisnis berubah dan perlu menambah fitur atau kategori barang baru (misalnya motor), maka kita cukup menambahkan file repository dan service tanpa mengubah kode awal. Kemudian, dengan mengimplementasikan SRP, jika kita ingin mengubah kode update produk, maka kecil kemungkinan terdapat bug pada daftar mobil. Dengan mengaplikasikan DIP, kita juga dapat melakukan testing lebih mudah pada kode kita.

3. Kerugian jika tidak menerapkan SOLID
- Tanpa SOLID, developer akan semakin sulit untuk menambahkan fitur dan me-maintain aplikasi, terutama jika codebasenya sudah sangat besar. Tanpa DIP, jika CarController bergantung langsung pada CarRepository yang konkret, jika kita memutuskan untuk pindah dari ArrayList ke database asli, maka kita perlu menulis ulang hampir seluruh kode di controller. Lalu, jika tidak menerapkan SRP, jika kita ingin mengubah aturan validasi kecil untuk jumlah produk, bisa saja secara tidak sengaja bisa merusak format warna mobil karena logikanya berada di satu tempat. Hal ini bisa menyebabkan bug yang terus menerus muncul dan susah diselesaikan. Kemudian, jika satu interface memiliki terlalu banyak method (melanggar ISP), setiap kali kita menambahkan fitur untuk mobil, maka kode untuk produk juga perlu dikompilasi ulang dan dites ulang.