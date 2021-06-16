Feature: API
  Scenario: api tests
    When получить токен гостя
    Then зарегистрировать игрока со следующими данными:
      |password|TestingPassword|
      |name|Test|
      |surname|Testovich|
      |currency_code|RUB|
    And авторизироватся созданным пользователем
    And запросить данные текущего пользователя
    And получить данные игрока с id 44