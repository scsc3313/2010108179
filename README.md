# Term Project 
* 김동하 : 2010108179

## 설계 및 구현
### 기본방향
* BatchController와 테스트를 기반으로 요구되는 클래스들 구현
* 배치작업의 흐름은 BatchController가 제어
* BatchController는 외부 요소와 직접적으로 의존관계를 갖지 않는다(IO등)
* DataProcessor및 Logger는 여러개를 사용할 수 있음
* 각각의 Processor는 추가된 순서대로 데이터를 처리한다
* 사용자의 선택에 따라 단일 스레드에서 순차처리 또는 다중 스레드에서 파이프라이닝 선택가능
* 일부 경우를 제외하고는 의존하는 객체를 외부에서 주입받는다
### 확장
* Processor
  * 정의된 데이터를 받아서 처리후 같은 형식으로 리턴.
  * 처리를 세부적으로 확장하고 싶다면 Processor에서 확장한다. 
* Logger
  * Formatter 및 실제 출력부분에서 확장.
  * Formatter는 외부에서 주입한다.
* Writer/Reader
* BatchController
  * 원한다면 BatchController Interface를 상속받아 구현 가능.
### 제약사항
* 한번 추가된 Processor와 Logger는 변경 불가능하다.
* 

## 실행방법/환경
* 프로젝트 디렉토리의 input.txt에 처리할 데이터를 위치시킨 후 실행
* 변경하고 싶다면 term.project.config에서 IOConfig에서 변경하면 된다.
* 객체간 의존관계는 ControllerConfig에서 정의됨.
