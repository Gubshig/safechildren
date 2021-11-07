# Safe Children

### 개요: Safe Children은 자녀의 승하차 여부와 실시간 위치 정보를 제공하므로써 안전한 통학에 대한 학부모에 대한 불안을 줄여주는 어플리케이션이다.

(동영상의 링크) 영상에 대한 간단한 설명

[![Safe Children](https://res.cloudinary.com/marcomontalbano/image/upload/v1636257948/video_to_markdown/images/youtube--mmNCXZOUrw0-c05b58ac6eb4c4700831b2b3070cd403.jpg)](https://www.youtube.com/watch?v=mmNCXZOUrw0 "Safe Children")
간단한 구동 영상


핵심기능 : 

승차하 정보 확인) nfc 태그를 활용하여 버스 내에 있는 nfc 리더기에 태그를 대면 학부모에게 알림이 간다.
학부모가 일일이 어플리케이션을 확인하지 않아도 알림이 가기 때문에 편의성이 좋다.

자녀 정보 조회 기능) firebase의 실시간 데이터베이스를 통해서 여러 자녀의 정보를 한눈에 알아볼 수 있다. 
한 자녀 가정만 고려한것이 아닌 다 자녀 가정도 고려한 방식이다.

자녀 위치 추적 기능) 구글 맵을 통해서 실시간으로 자녀의 위치를 알 수 있고 좌표정보도 볼 수 있다.
이를 통해 학부모의 걱정을 덜어줄 수 있고 직관적인 정보를 제공할 수 있다.
 
실행방법 :

테스트용 학부모 계정을 이용해서 위에서 명시한 핵심 기능들을 체험해볼 수 있다.
1. 메인 화면이 시작되면 알려준 계정을 사용해서 로그인(회원가입도 가능)
 <img src="https://user-images.githubusercontent.com/40236418/140608944-482b7621-d4f2-43f3-a443-0de241e076ef.jpg" width="150" height="300"/>
 
2. 로그인 후 메인 탭에서 자녀 관리
<img src="https://user-images.githubusercontent.com/40236418/140608940-82fb67d2-82ca-4cd6-a00a-2e94c938163e.jpg" width="150" height="300"/>

3. 자녀정보 탭에서 자녀 정보 확인
<img src="https://user-images.githubusercontent.com/40236418/140608941-b13cb6fb-8648-4a05-8938-f0055fa79930.jpg" width="150" height="300"/>

4. 위치/현황 탭에서 자녀 위치 실시간 확인
<img src="https://user-images.githubusercontent.com/40236418/140608942-6e7e4034-4a94-4620-8ec5-7fa980b470a6.jpg" width="150" height="300"/>

계정생성 시

1. 이메일 인증 후 서비스를 사용 가능하다
<img src="https://user-images.githubusercontent.com/40236418/140609184-880f32af-d929-48d8-b9e5-9ea6f3b80953.jpg" width="150" height="300"/>

2. 이메일 인증 후 본인의 정보 입력이 가능하고 자녀 등록 밑 위에서 설명한 기능 사용 가능


구동환경
minSdkVersion 21
targetSdkVersion 30

사용자가 학생일 경우 승하차 상태를 업데이트 해야하기에 nfc기능이 필수이다.
