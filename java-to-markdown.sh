#!/bin/bash

# Configurações
DOCS_DIR="documentation"
STRUCTURE_FILE="${DOCS_DIR}/index.md"

# Criar diretório de documentação
mkdir -p "${DOCS_DIR}"
touch documentation.md
# Processar todos os arquivos .java recursivamente
find . -type f -name "*.java" -print0 | while IFS= read -r -d '' file; do
    # Criar estrutura de diretórios correspondente
#    md_file="${DOCS_DIR}/${file%.java}.md"
    md_file="documentation.md"
    mkdir -p "$(dirname "$md_file")"

    # Escrever conteúdo do arquivo .md
    {
        echo "# ${file}"
        echo
        echo '```java'
        cat "$file"
        echo -e '\n```\n'
    } >> "$md_file"

done

# Gerar estrutura de pastas formatada
{
    echo "# Estrutura do Projeto"
    echo
    echo '```'
    find src -type d | sed -e 's/\/[^/]*$/\|-- &/' -e 's/\/[^/]*\//|   /g' -e 's/\(.*\)--/\1\/--/'
    echo '```'
} > "${STRUCTURE_FILE}"