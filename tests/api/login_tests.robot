*** Settings ***
Library     RequestsLibrary
Resource    ../resources/variables.robot

Suite Setup    Create Session    api    ${BASE_URL}    verify=False

*** Test Cases ***
Login com credenciais válidas retorna 200
    ${body}=    Create Dictionary    email=${ADMIN_EMAIL}    password=${ADMIN_PASS}
    ${resp}=    POST On Session    api    /auth/login    json=${body}
    Should Be Equal As Strings    ${resp.status_code}    200
    Should Be Equal As Strings    ${resp.json()['status']}    True
    Should Be Equal As Strings    ${resp.json()['message']}    Usuário realizou login com sucesso

Login com senha errada retorna erro
    ${body}=    Create Dictionary    email=${ADMIN_EMAIL}    password=senhaerrada
    ${resp}=    POST On Session    api    /auth/login    json=${body}    expected_status=any
    Should Be True    ${resp.status_code} >= 400

Login com email inexistente retorna erro
    ${body}=    Create Dictionary    email=naoexiste@teste.com    password=Ab@12345
    ${resp}=    POST On Session    api    /auth/login    json=${body}    expected_status=any
    Should Be True    ${resp.status_code} >= 400

Login com body vazio retorna erro
    ${body}=    Create Dictionary
    ${resp}=    POST On Session    api    /auth/login    json=${body}    expected_status=any
    Should Be True    ${resp.status_code} >= 400

Logout após login funciona
    ${body}=    Create Dictionary    email=${ADMIN_EMAIL}    password=${ADMIN_PASS}
    ${resp_login}=    POST On Session    api    /auth/login    json=${body}
    Should Be Equal As Strings    ${resp_login.status_code}    200
    ${resp_logout}=    DELETE On Session    api    /auth/logout
    Should Be Equal As Strings    ${resp_logout.status_code}    200
    Should Be Equal As Strings    ${resp_logout.json()['status']}    True