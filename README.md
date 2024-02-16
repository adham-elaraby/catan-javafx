# Vorlage zum FOP-Projekt

Beachten Sie die Hinweise zum Herunterladen, Importieren, Bearbeitern, Exportieren und Hochladen in unserem
[Studierenden-Guide](https://wiki.tudalgo.org/)

# Install Github Client

MacOS: `brew install gh`

Login: run `gh auth login` and choose website login

clone the repo, and run the following commands in the directory:


# !IMPORTANT! for our private Repo!

- Add the public repo as public
`git remote add public https://github.com/FOP-2324/FOP-2324-Projekt-Student.git`

- Make git not change `build.gradle.kts` because we all have different tu-id's
`git update-index --assume-unchanged build.gradle.kts`

- To Fetch:

To pull any changes from public `git pull public main`
To pull each others private changes `git pull origin main`
To push your commits to private `git push origin main`
