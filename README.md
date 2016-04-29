# BrinquedoIOT
Aplicação com joystick para controlar robos trekking(ou carrinhos)..

Possibilidade de conectar com qualquer modulo bluetooth serial que ja esteja pareado com o aparelho.

## Instalação do ambiente de trabalho ##

Para desenvolver a aplicação, foi usado o ambiente de trabalho do eclipse usando ADT-plugin.

<a href="https://github.com/davidfstr/rdiscount" title="RDiscount">Link para instalação</a>

## Instruções para importar projeto e demais dependencias##

Depois de ter instalado o ambiente e iniciando,por padrão o eclipse criou uma pasta chamada  (workspace) dentro da pasta do nome do usuario do computador,isto seria em
 C:\Users\Administrador\workspace, ou apenas va ao local aonde indocou para ele criar essa pasta.

Dentro da pasta workspace com o git instalado, com botao direito click em (Git bash here).
Adicione a linha de comando o codigo para clonar o repositorio (git clone https://github.com/IgorFachini/BrinquedoIOT.git).

Apos isso va ate o eclipse.

1. file>import>android>Existing android code...>browse
2. Indicar a pasta (workspace)>Selecionar  BrinquedoIOT\android-support-v7-appcompat e BrinquedoIOT\Controle BT arduino.>Finish
3. 

## Obs Para erros de importação do projeto ou dependencias. ##


**Se houver um ponto de exclamação vermelho no icone de pastas do projeto...**

Ha uma dependencia que precisa ser adicionada...

1. Em eclipse ,botao direito en cima do projeto >> Build path >> configure Build path.
2. na aba Libraries clique no arquivo android-support-v4 >> Edit,
3. indique o caminho  dentro do projeto na pasta libs o arquivo android-support-v4
