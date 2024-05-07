context('add message', () => {
    let msgText = 'msg' + new Date().getTime();

    describe('Basic Suite', () => {
        it('login', () => {
            cy.fixture('settings').then(settings => {
                cy.visit(settings.baseUrl);
                cy.loginAdmin();
            });
        });
    });

});
