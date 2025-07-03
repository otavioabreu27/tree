<div align="center">
  <h1>Tree</h1>
  <img src="docs/TreeRounded.png" alt="Logo do Tree" width="150"/>
</div>

<p align="center">
  <a href="#sobre">Sobre</a> •
  <a href="#por-que-usar-o-tree">Por que</a> •
  <a href="#funcionalidades">Funcionalidades</a> •
  <a href="#instalação">Instalação</a>
</p>

---

<span id="sobre"></span>

## 📘 Sobre

**Tree** é uma ferramenta de linha de comando que **analisa projetos Maven** e **gera grafos de dependência** entre módulos. Ideal para entender e visualizar como seus projetos se conectam.

---

<span id="por-que-usar-o-tree"></span>

## ❓ Por que usar o Tree?

Em projetos com múltiplos módulos interdependentes, qualquer alteração pode impactar diretamente outras partes da aplicação. Entender essas relações é essencial para:

- 🔍 **Antecipar impactos** antes de uma alteração ser feita  
- 🛡️ **Mitigar riscos** de quebras inesperadas em outros módulos  
- 📊 **Visualizar a arquitetura** de dependências de forma clara e navegável  

Com o **Tree**, você tem uma visão abrangente do seu ecossistema Maven — facilitando decisões técnicas mais seguras e estratégicas.

---

<span id="funcionalidades"></span>

## 🚀 Funcionalidades

- 🔍 Busca recursivamente por arquivos `pom.xml`
- 📦 Registra os módulos e suas dependências
- 🕸️ Constrói um grafo com base nas relações entre os módulos
- 📤 Exporta o grafo nos formatos: `HTML`, `CSV` ou `JSON`

---

<span id="instalação"></span>

## 📦 Instalação

```bash
curl -sSL https://raw.githubusercontent.com/usuario/repositorio/main/install.sh | bash
