#Instruções de como usar esta Biblioteca

- ###Como usar o modelo de dados
1. Criar objetos do formato Json usando as classes JAraay, JBoolean, JNumber, JObject, JString, JVlaue
2. Usar a função toJsonString para obter os elementos textuais em String
3. Pode recorrer as funçoes countObj para contar o numero de objetos, getName para obter o nome de um determinado array e
a função getStrings para obter todas as strings de um objeto
   
- ###Como converter qualquer objeto para o formato Json
1. Criar um objeto qualquer
2. Usar a função toJson que vai que vai instanciar automaticamente o modelo anterior 

- ###Como desonvolver os pulgins para o visualizador
Para desonvolver um plugin de visualização do esqueleto da árvore terá que criar uma classe no ficheiro Plugins.kt do tipo 
FrameSetup com as alterações necessárias para a árvore e colocar o nome da classe criada à frente de JsonTreeSkeleton.setup= no ficheiro di.properties

Para desenvolver um plugin de ações, isto é desenvolver um botão, bastar criar uma classe no ficheiro Plugins.kt do tipo Action e o nome da classe criada à frente de JsonTreeSkeleton.actions= no ficheiro di.properties

note que no caso da criação de uma ação pode usar varias ao mesmo tempo no ficheiro di.properties desde que sejam separadas por uma vírgula