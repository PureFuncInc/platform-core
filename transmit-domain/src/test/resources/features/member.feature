Feature: 推播相關

  發送電子郵件、簡訊<br>

  Scenario: 1.1.1.1
            -> 舉例: 輸入電子郵件、內容，回傳發送成功
    Given Pass email and content
    When
    Then Login response should be success

  Scenario: 1.1.1.2 用戶名稱登入失敗
            -> 舉例: 輸入 用戶名稱 和 錯誤密碼，回傳登入失敗
    Given Pass username and wrong password
    When Login by username
    Then Login response should be fail

  Scenario: 1.1.2.1 電子郵件登入成功
            -> 舉例: 輸入 電子郵件 和 密碼，回傳登入成功
    Given Pass email and password
    When Login by email
    Then Login response should be success

  Scenario: 1.1.2.2 電子郵件登入失敗
            -> 舉例: 輸入 電子郵件 和 錯誤密碼，回傳登入失敗
    Given Pass email and wrong password
    When Login by email
    Then Login response should be fail

  Scenario: 1.1.3.1 註冊成功
            -> 舉例: 輸入 電子郵件，回傳註冊成功
    Given Pass email
    When Signup
    Then Signup response should be success

  Scenario: 1.1.3.1 註冊失敗
            -> 舉例: 輸入 電子郵件，回傳註冊失敗
    Given Pass existed email
    When Signup
    Then Signup response should be fail

  Scenario: 1.1.4.1 登出
            -> 舉例: 輸入用戶ID，回傳登出成功
    Given Pass user id
    When Logout
    Then Logout response should be success

#  Scenario: 1.1.4.1 忘記密碼重設成功
#            -> 舉例: 輸入 用戶名稱 和 電子郵件，發送新密碼到電子郵件
#    Given Pass username and email
#    When ResetPassword
#    Then Transfer client send the mail
#
#  Scenario: 1.1.4.2 忘記密碼重設失敗
#            -> 舉例: 輸入 用戶名稱 和 電子郵件，用戶名稱跟電子郵件不符合
#    Given Pass username and wrong email
#    When ResetPassword
#    Then ResetPasswordResponse
#
#  Scenario: 1.1.5 修改密碼
#            -> 舉例: 輸入 舊密碼 和 新密碼，回傳修改結果
#    Given Pass a Challenge object
#    When Add
#    Then Challenge id should not be null

#
#
#
#  Scenario: 1.2.1
#            -> 舉例: BO 首次進入 LINE PAY 交易記錄頁面
#    Given Pass default page
#    When Query all
#    Then Challenge list size should greater than 0