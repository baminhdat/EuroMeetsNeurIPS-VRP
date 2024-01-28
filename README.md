# Nội dung cơ bản
Đây là kết quả cuối cùng của đề tài nghiên cứu của bộ môn Project I - IT3150 của chương trình đào tạo Khoa Học Máy Tính - IT1 của trường Công Nghệ Thông Tin và Truyền Thông, Đại Học Bách Khoa Hà Nội.

Tên đề tài là "Tìm hiểu và áp dụng cơ bản các giải thuật Heuristics trong giải bài toán định tuyến phương tiện có ràng buộc."
## Bài toán định tuyến phương tiện đặt ra

Bài toán định tuyến phương tiện được sử dụng để nghiên cứu là bài toán VRP của cuộc thi Euro meets NeurIPS VRP Competition 2022.

Thông tin chi tiết về bài toán có thể được tìm thấy ở đây:
"https://euro-neurips-vrp-2022.challenges.ortec.com/assets/pdf/euro-neurips-vrp-2022-rules-2-aug.pdf"

Dữ liệu đầu vào của bài toán là một số Instances trong các Instances công khai của vòng đầu tiên, vòng QuickStart của cuộc thi.
## Giải thuật Heuristics được sử dụng
Muốn giải một bài toán định tuyến phương tiện với nhiều phương tiện thì ta cần thực hiện 2 công việc:

1.Phân phối các đơn hàng cho các phương tiện

2.Xây dựng một lộ trình tối ưu từ các đơn hàng đó

Giải thuật di truyền Genetic Algorithm được áp dụng để giải quyết công việc thứ nhất. 

Hai giải thuật Heuristic hướng xây dựng được áp dụng để hoàn thành công việc thứ hai, chúng là:

+Giải thuật Safest Neighbour: lấy ý tưởng từ giải thuật Nearest Neighbour, tiến hành xây dựng lời giải từng bước bằng cách thêm hàng xóm "an toàn" nhất của địa điểm hiện tại vào cuối lộ trình

+Giải thuật Cheapest Insert: do hiệu quả khi chỉ áp dụng giải thuật trên là chưa cao nên giải thuật Cheapest Insert được sử dụng để tiếp tục xây dựng các lộ trình thông qua cơ chế trì hoãn một đơn hàng.

Thông tin cụ thể về cấu trúc, cách hoạt động - cài đặt của các giải thuật cũng như bài toán có thể được tìm thấy ở đây:

"https://drive.google.com/drive/folders/16YJ7C4zXm_d-_rfMj0ZHJtsWAiYR3v3v?usp=drive_link"

Kết quả giải thuật có thể được tìm thấy ở đây:

"https://drive.google.com/drive/folders/19mst5fuZO-OQ55XQ3_O5-XSK3cmGjW44?usp=drive_link"
### Hiệu quả của giải thuật chưa cao nhưng không gian cải thiện là lớn.
## Cách thức cài đặt

1. Cần có một IDE để tương tác với mã nguồn, ví dụ IntelliJ và Oracle OpenJDK 18. trở lên

2. Clone toàn bộ mã nguồn về hoặc tải về dưới dạng file .zip sau đó sử dụng IDE để mở mã nguồn

3. Chuẩn hóa lại đường dẫn dẫn tới thư mục Problem chứa các Instances của bài toán ở dòng thứ 39 của lớp InputLoader

4. Build và Run lớp Main, nhập vào tên Instance ở dòng thứ nhất và dạng biến thể muốn giải (Static - tĩnh, Dynamic - động) ở dòng thứ hai.

(Do mục tiêu của đề tài là đánh giá hiệu quả của thuật giải nên chỉ nên sử dụng kết quả của biến thể tĩnh)
## Liên lạc và cải thiện

Mọi ý kiến đóng góp và cải thiện thuật toán có thể được gửi đến cho tôi qua địa chỉ gmail bminhdat03.p@gmail.com.
