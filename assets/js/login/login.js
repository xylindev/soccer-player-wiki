export const sendAlert = () => {
    const parameter = new URLSearchParams(document.location.search)
    const auth = parameter.get("auth")
    
    if(auth === "false")
        alert("Échec de l'authentification : mot de passe incorrect.")
}