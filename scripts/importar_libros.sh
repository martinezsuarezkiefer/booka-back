#!/bin/bash

temas=(
  # ROMANCE GENERAL
"subject:romance"
"subject:romance fiction"
"subject:love stories"
"subject:contemporary romance"
"subject:historical romance"
"subject:fantasy romance"
"subject:dark romance"
"subject:paranormal romance"
"subject:romantic fantasy"
"subject:romantic suspense"
"subject:romantic comedy"
"subject:new adult romance"
"subject:young adult romance"
"subject:adult romance"
"subject:college romance"
"subject:small town romance"
"subject:billionaire romance"
"subject:office romance"
"subject:sports romance"
"subject:royal romance"
"subject:forbidden romance"
"subject:enemies to lovers"
"subject:friends to lovers"
"subject:second chance romance"
"subject:slow burn romance"
"subject:marriage of convenience"
"subject:fake dating romance"
"subject:dark academia romance"
"subject:gothic romance"

# LGBTQ+ ROMANCE
"subject:lgbt romance"
"subject:gay romance"
"subject:lesbian romance"
"subject:queer romance"

# ROMANCE FANTASY / DARK
"subject:dark fantasy romance"
"subject:vampire romance"
"subject:wolf shifter romance"
"subject:witch romance"
"subject:fairy romance"
"subject:fae romance"
"subject:monster romance"

# ROMANCE MANGA / MANHWA
"subject:romance manga"
"subject:shojo romance"
"subject:josei romance"
"subject:romantic manga"
"subject:school romance manga"
"subject:fantasy romance manga"
"subject:slice of life romance manga"
"subject:romance manhwa"

# AUTORES / FRANQUICIAS QUE FUNCIONAN MUY BIEN
"subject:colleen hoover"
"subject:sarah j maas"
"subject:emily henry"
"subject:ali hazelwood"
"subject:ana huang"
"subject:elsie silver"
"subject:the love hypothesis"
"subject:twisted love"
"subject:a court of thorns and roses"
)


maxLibros=120
maxPorPagina=20

for tema in "${temas[@]}"
do
  echo "Importando libros del tema: $tema ..."

  temaEncoded=$(printf '%s' "$tema" | sed 's/ /%20/g' | sed 's/:/%3A/g')

  url="http://localhost:9090/libros/importar?tema=$temaEncoded&maxLibros=$maxLibros&maxPorPagina=$maxPorPagina"

  echo "$url"

  curl -X POST "$url"

  echo ""

  echo "Importación de '$tema' completada"

  sleep 7
done
