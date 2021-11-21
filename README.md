# Safe Children

### 개요: Safe Children은 자녀의 승하차 여부와 실시간 위치 정보를 제공함으로서 안전한 통학에 대한 학부모에 대한 불안을 줄여주는 어플리케이션이다

### 개발동기
2018년 지난 7월14일 동두천에서 어린이가 통학차에서 내리지 못한 채 7시간 방치돼 숨진 매우 안타까운 사고가 있었다. 사고 원인은 운전기사분이 부주의한 인원 확인으로 밝혀졌는데, 때문에 어린이들의 안전을 위한 별도의 스마트 안전 보조장치가 필요함을 느꼈다. 그래서 자녀의 통학에 대한 학부모의 걱정을 줄여줄 수 있는 애플리케이션을 만들어야겠다고 생각했다. 

### 시나리오

parent, student 공통

계정 만들기 : email, password 기입 후 메일 전송, 전송된 이메일 내 링크를 클릭하여 이메일 인증 완료

로그인 : 이메일 인증된 아이디, 패스워드로 로그인


1. user type : Parent

기본 정보 입력 : 이름, 성별, 생년월일, 학부모 checkbox 선택 시 자녀 목록 생성

자녀 추가 : 자녀의 이메일로 자녀 추가 가능 ( 이메일만 알면 해당 자녀 승하차시 위치 정보를 얻어올 수
있다는 점에서 개인정보 이슈가 있음. )

 +) 이미 등록된 자녀 or 해당 이메일이 firebase database에 없을 시 등록 불가

자녀 정보 확인 : 자녀 이름, 이메일, 성별, 생년월일, 마지막으로 등록된 위치, 승하차 상태, 학교 / 학원,
마지막 위치 업데이트 시각 정보 확인 가능

위치 / 현황 : DB에 저장된 위치에 따라 마커 확인 가능, 마커 클릭 시 해당 자녀의 이름 표시

2. user type : Student

기본 정보 입력 : 이름, 성별, 생년월일, 학교 / 학원 검색 후 해당 정보 등록 가능

승하차 시 nfc 태그 :
하차 상태에서  태그 -> 승차 상태로 변경 후 자신을 자녀로 추가한 유저에게 notification 발송
승차 상태에서 정해진 시각 or 일정 거리 이상 이동할 때마다 위치 정보를 업데이트, 학부모가 확인 가능

승차 상태에서 태그 -> 하차 상태로 변경 후 gps 해제, 자신을 자녀로 추가한 유저에게 notification 발송

### 간단한 구동 영상

### 학부모 계정 구동영상

[![Safe Children](https://res.cloudinary.com/marcomontalbano/image/upload/v1636257948/video_to_markdown/images/youtube--mmNCXZOUrw0-c05b58ac6eb4c4700831b2b3070cd403.jpg)](https://www.youtube.com/watch?v=mmNCXZOUrw0 "Safe Children")

### 학생 계정 구동영상

[![Safe Children student account](https://res.cloudinary.com/marcomontalbano/image/upload/v1636860258/video_to_markdown/images/youtube--VnavDQ_veJg-c05b58ac6eb4c4700831b2b3070cd403.jpg)](https://youtu.be/VnavDQ_veJg "Safe Children student account")

### 학생 계정 nfc 태그

[![Safe Children student NFC tag ](https://res.cloudinary.com/marcomontalbano/image/upload/v1636860319/video_to_markdown/images/youtube--zGYGTAdUOUA-c05b58ac6eb4c4700831b2b3070cd403.jpg)](https://youtu.be/zGYGTAdUOUA "Safe Children student NFC tag ")

### 핵심기능 : 

승차하 정보 확인) nfc 태그를 활용하여 버스 내에 있는 nfc 리더기에 태그를 대면 학부모에게 알림이 간다.
학부모가 일일이 어플리케이션을 확인하지 않아도 알림이 가기 때문에 편의성이 좋다.

자녀 정보 조회 기능) firebase의 실시간 데이터베이스를 통해서 여러 자녀의 정보를 한눈에 알아볼 수 있다. 
한 자녀 가정만 고려한것이 아닌 다 자녀 가정도 고려한 방식이다.

자녀 위치 추적 기능) 구글 맵을 통해서 실시간으로 자녀의 위치를 알 수 있고 좌표정보도 볼 수 있다.
이를 통해 자녀의 안전에 대한 걱정을 덜어줄 수 있고 직관적인 정보를 제공할 수 있다.

자녀 학교/학원 등록) 나이스와 커리어넷 api를 활용하여 자녀의 학교/학원을 등록할 수 있다.
 
### 실행방법 :

테스트용 학부모 계정을 이용해서 위에서 명시한 핵심 기능들을 체험해볼 수 있다.
### 1. 메인 화면이 시작되면 알려준 계정을 사용해서 로그인(회원가입도 가능)
 <img src="https://user-images.githubusercontent.com/40236418/140608944-482b7621-d4f2-43f3-a443-0de241e076ef.jpg" width="150" height="300"/>
 
### 2. 로그인 후 메인 탭에서 자녀 관리
<img src="https://user-images.githubusercontent.com/40236418/140608940-82fb67d2-82ca-4cd6-a00a-2e94c938163e.jpg" width="150" height="300"/>

### 3. 자녀정보 탭에서 자녀 정보 확인
<img src="https://user-images.githubusercontent.com/40236418/140608941-b13cb6fb-8648-4a05-8938-f0055fa79930.jpg" width="150" height="300"/>

### 4. 위치/현황 탭에서 자녀 위치 실시간 확인
<img src="https://user-images.githubusercontent.com/40236418/140608942-6e7e4034-4a94-4620-8ec5-7fa980b470a6.jpg" width="150" height="300"/>

### 계정생성 시

1. 이메일 인증 후 서비스를 사용 가능하다
<img src="https://user-images.githubusercontent.com/40236418/140609184-880f32af-d929-48d8-b9e5-9ea6f3b80953.jpg" width="150" height="300"/>

2. 이메일 인증 후 본인의 정보 입력이 가능하고 자녀 등록 밑 위에서 설명한 기능 사용 가능


### 구동환경
minSdkVersion 21
targetSdkVersion 30

사용자가 학생일 경우 승하차 상태를 업데이트 해야하기에 nfc기능이 필수이다.
