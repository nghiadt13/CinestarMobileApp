MAyApp/
└── app/
└── src/main/
├── kotlin+java/
│   └── com/example/mayapp/
│       │
│       ├── core/                  # LÕI (CORE): Chứa các thành phần cơ sở dùng chung cho toàn ứng dụng.
│       │   ├── database/          #   - Quản lý cơ sở dữ liệu (Room DB, DAOs).
│       │   ├── di/                #   - Dependency Injection (Hilt, Dagger, Koin).
│       │   └── network/           #   - Quản lý kết nối mạng (Retrofit, API services).
│       │
│       ├── feature/               # TÍNH NĂNG (FEATURE): Mỗi thư mục con là một tính năng độc lập.
│       │   └── auth/              #   - Ví dụ: Module tính năng "Xác thực" (Authentication).
│       │       │
│       │       ├── data/          #   (MODEL): Lớp dữ liệu của tính năng.
│       │       │   ├── local/     #     - Nguồn dữ liệu local (từ database).
│       │       │   ├── remote/    #     - Nguồn dữ liệu remote (từ API).
│       │       │   ├── model/     #     - Các lớp dữ liệu (Data Models/DTOs).
│       │       │   ├── mapper/    #     - Ánh xạ (map) dữ liệu giữa các lớp (e.g., DTO -> Domain Model).
│       │       │   └── repository/#     - Triển khai Repository, là nguồn cung cấp dữ liệu duy nhất.
│       │       │
│       │       ├── domain/        #   (DOMAIN): Lớp chứa logic nghiệp vụ cốt lõi, độc lập với UI và data.
│       │       │   ├── model/     #     - Các đối tượng nghiệp vụ (Business Objects).
│       │       │   └── usecase/   #     - Các Use Case xử lý một nhiệm vụ cụ thể.
│       │       │
│       │       ├── ui/            #   (VIEW): Lớp giao diện người dùng của tính năng.
│       │       │   ├── activity/  #     - Chứa các Activity.
│       │       │   │   
│       │       │   │  
│       │       │   │   
│       │       │   │  
│       │       │   │   
│       │       │   │   
│       │       │   │
│       │       │   ├── adapter/   #     - Các RecyclerView Adapters.
│       │       │   └── ...        #     - Có thể chứa Fragment, Dialog, Custom View...
│       │       │
│       │       └── viewmodel/     #   (VIEWMODEL): Cung cấp dữ liệu và xử lý logic cho lớp View.
│       │
│       └── ui/                    # GIAO DIỆN CHUNG (COMMON UI): Các thành phần UI có thể tái sử dụng.
│           ├── extension/         #   - Các hàm mở rộng (extension functions) cho View.
│           ├── theme/             #   - Định nghĩa Theme, màu sắc, kiểu chữ.
│           └── view/              #   - Các Custom View dùng chung.
│
└── manifests/
└── AndroidManifest.xml