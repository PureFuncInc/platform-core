Feature: 支付相關

  in this feature, we have <b>Challenge</b>, <b>DefaultPage</b> object<br>

  Scenario: 1.1.1 產生支付連結
            -> 舉例: 填入相關資訊之後，回應一個URL (之後做成 QR CODE
    Given Pass a Challenge object
    When Add
    Then Challenge id should not be null

  Scenario: 1.2.1 查詢交易記錄
            -> 舉例: BO 首次進入 LINE PAY 交易記錄頁面
    Given Pass default page
    When Query all
    Then Challenge list size should greater than 0